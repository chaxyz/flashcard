package com.comkub.flashcardbackend.services;

import com.comkub.flashcardbackend.dto.CardDTO;
import com.comkub.flashcardbackend.entity.Card;
import com.comkub.flashcardbackend.entity.Deck;
import com.comkub.flashcardbackend.exception.NotFoundException;
import com.comkub.flashcardbackend.repository.CardRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCard(int id){
        return  cardRepository.findAllByDeck_Id(id);
    }

    public Card createNewCard(Card card){
        return  cardRepository.save(card);
    }

    public Card getCardById(int id){
        return cardRepository.findById(id).orElseThrow(() -> new NotFoundException("Card id " + id + " found"));
    }

    public Card editCard(int cardId,CardDTO cardDTO){
        Card card = getCardById(cardId);
        card.setCardDescription(cardDTO.getCardDescription());
        card.setCardTitle(cardDTO.getCardTitle());
        card.setCardMeaning(cardDTO.getCardMeaning());
        return  card;
    }

    public void deleteCard(int cardId){
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }

    public void processFile(MultipartFile file , Deck deck) throws IOException {
        System.out.println("test");
        String content;
        if (file.getContentType().equals("text/plain")) {
            content = new String(file.getBytes());
        } else if (file.getContentType().equals("application/pdf")) {
            content = extractTextFromPdf(file);
        } else {
            throw new IllegalArgumentException("Invalid file type. Only text or PDF files are allowed.");
        }

        saveEntriesToDatabase(content, deck);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();
        return text;
    }


    private void saveEntriesToDatabase(String content,Deck deck) {
        Pattern pattern = Pattern.compile("\"(.*?)\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String word = matcher.group(1);
            String meaning = matcher.group(2);

            Card card = Card.builder().cardTitle(word).cardMeaning(meaning).deck(deck).build();
            cardRepository.save(card);
        }
    }
}
