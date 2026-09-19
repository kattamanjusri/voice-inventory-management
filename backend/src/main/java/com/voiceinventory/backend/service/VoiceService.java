package com.voiceinventory.backend.service;

import com.voiceinventory.backend.dto.StockUpdateRequest;
import com.voiceinventory.backend.dto.VoiceResponse;
import com.voiceinventory.backend.entity.Product;
import com.voiceinventory.backend.exception.BadRequestException;
import com.voiceinventory.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoiceService {

    private final ProductRepository productRepository;
    private final StockService stockService;

    public VoiceService(ProductRepository productRepository,
                        StockService stockService) {
        this.productRepository = productRepository;
        this.stockService = stockService;
    }

    public VoiceResponse processVoice(String text) {

        if (text == null || text.trim().isEmpty()) {
            throw new BadRequestException("Voice text is required.");
        }

        String originalText = text.trim();
        String input = originalText.toLowerCase().trim();

        // -----------------------------------
        // 1. Convert Telugu words to English
        // -----------------------------------

        // Telugu action mappings
        input = input
                .replace("ఆడ్ చెయ్", "add")
                .replace("ఆడ్ చేయి", "add")
                .replace("అడ్ చెయ్", "add")
                .replace("అడ్ చేయి", "add")
                .replace("రిమూవ్ చెయ్", "remove")
                .replace("రిమూవ్ చేయి", "remove");

        // Telugu unit mappings
        input = input
                .replace("బ్యాగ్స్", "bags")
                .replace("బ్యాగులు", "bags")
                .replace("కిలోలు", "kg")
                .replace("కిలో", "kg")
                .replace("కేజీ", "kg")
                .replace("లీటర్లు", "litres")
                .replace("పీసెస్", "pieces")
                .replace("బాక్సులు", "boxes")
                .replace("డజన్లు", "dozens")
                .replace("క్వింటాళ్లు", "quintals");

        // Telugu product mappings
        input = input
                .replace("రైస్", "rice")
                .replace("షుగర్", "sugar")
                .replace("బిస్కెట్స్", "biscuits")
                .replace("కుకింగ్ ఆయిల్", "cooking oil")
                .replace("ఎగ్స్", "eggs")
                .replace("సోప్", "soap")
                .replace("వీట్ ఫ్లోర్", "wheat flour");

        // -----------------------------------
        // 2. Find action
        // -----------------------------------

        String action = findAction(input);

        if (action == null) {
            throw new BadRequestException(
                    "Could not understand the action. Say add or remove."
            );
        }

        // -----------------------------------
        // 3. Find quantity
        // -----------------------------------

        input = input
                .replaceAll("\\bone\\b", "1")
                .replaceAll("\\btwo\\b", "2")
                .replaceAll("\\bthree\\b", "3")
                .replaceAll("\\bfour\\b", "4")
                .replaceAll("\\bfive\\b", "5")
                .replaceAll("\\bsix\\b", "6")
                .replaceAll("\\bseven\\b", "7")
                .replaceAll("\\beight\\b", "8")
                .replaceAll("\\bnine\\b", "9")
                .replaceAll("\\bten\\b", "10");

        Pattern quantityPattern =
                Pattern.compile("(\\d+(?:\\.\\d+)?)");

        Matcher quantityMatcher =
                quantityPattern.matcher(input);

        if (!quantityMatcher.find()) {
            throw new BadRequestException(
                    "Could not understand the quantity."
            );
        }

        BigDecimal quantity =
                new BigDecimal(quantityMatcher.group(1));

        // -----------------------------------
        // 4. Find unit
        // -----------------------------------

        String[] units = {
                "pieces",
                "piece",
                "kg",
                "kgs",
                "bags",
                "bag",
                "cartons",
                "carton",
                "boxes",
                "box",
                "dozens",
                "dozen",
                "litres",
                "liters",
                "litre",
                "liter",
                "quintals",
                "quintal"
        };

        String unit = null;

        for (String currentUnit : units) {

            if (input.contains(currentUnit)) {
                unit = currentUnit;
                break;
            }
        }

        if (unit == null) {
            throw new BadRequestException(
                    "Could not understand the unit. " +
                    "Use pieces, kg, bags, cartons, boxes, dozens, litres or quintals."
            );
        }

        // -----------------------------------
        // 5. Extract product name
        // -----------------------------------

        String productName = input;

        // Remove action
        productName = productName.replaceAll(
                "(?i)\\b" + Pattern.quote(action) + "\\b",
                ""
        );

        // Remove quantity
        productName = productName.replace(
                quantityMatcher.group(1),
                ""
        );

        // Remove unit
        productName = productName.replace(
                unit,
                ""
        );

        // Clean extra spaces
        productName = productName
                .replaceAll("\\s+", " ")
                .trim();

        if (productName.isEmpty()) {
            throw new BadRequestException(
                    "Could not understand the product name."
            );
        }

        // -----------------------------------
        // 6. Find product
        // -----------------------------------

        List<Product> products =
                productRepository
                        .findByNameContainingIgnoreCaseOrderByNameAsc(
                                productName
                        );

        if (products.isEmpty()) {
            throw new BadRequestException(
                    "Product '" + capitalizeWords(productName) +
                    "' was not found in inventory."
            );
        }

        Product product = products.get(0);

        // -----------------------------------
        // 7. Update stock
        // -----------------------------------

        StockUpdateRequest request =
                new StockUpdateRequest(quantity, unit);

        Product updatedProduct;

        if ("add".equalsIgnoreCase(action)) {

            updatedProduct =
                    stockService.addStock(
                            product.getId(),
                            request
                    );

        } else {

            updatedProduct =
                    stockService.removeStock(
                            product.getId(),
                            request
                    );
        }

        // -----------------------------------
        // 8. Response
        // -----------------------------------

        VoiceResponse response = new VoiceResponse();

        response.setOriginalText(originalText);
        response.setAction(action.toUpperCase());
        response.setProductName(updatedProduct.getName());
        response.setQuantity(quantity);
        response.setUnit(unit);

        String message;

        if ("add".equalsIgnoreCase(action)) {

            message =
                    "Added " + quantity.stripTrailingZeros().toPlainString()
                    + " " + unit + " of "
                    + updatedProduct.getName()
                    + ". Current stock: "
                    + updatedProduct.getQuantity().stripTrailingZeros().toPlainString()
                    + " " + updatedProduct.getUnit();

        } else {

            message =
                    "Removed " + quantity.stripTrailingZeros().toPlainString()
                    + " " + unit + " of "
                    + updatedProduct.getName()
                    + ". Current stock: "
                    + updatedProduct.getQuantity().stripTrailingZeros().toPlainString()
                    + " " + updatedProduct.getUnit();
        }

        response.setMessage(message);

        return response;
    }

    private String findAction(String input) {

        if (input.matches(".*\\badd\\b.*")) {
            return "add";
        }

        if (input.matches(".*\\bremove\\b.*")) {
            return "remove";
        }

        return null;
    }

    private String capitalizeWords(String text) {

        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {

            if (!word.isEmpty()) {

                result.append(
                        Character.toUpperCase(word.charAt(0))
                );

                if (word.length() > 1) {
                    result.append(word.substring(1));
                }

                result.append(" ");
            }
        }

        return result.toString().trim();
    }
}
