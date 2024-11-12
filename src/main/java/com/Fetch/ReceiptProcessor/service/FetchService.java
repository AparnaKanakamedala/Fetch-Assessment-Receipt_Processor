package com.Fetch.ReceiptProcessor.service;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fetch.ReceiptProcessor.model.Reciept;
import com.Fetch.ReceiptProcessor.repositories.PointsRepository;

@Service
public class FetchService {

    @Autowired
    PointsRepository pointsCache;

    // Method recieves reciept payload, generates and stores reciept points and id,
    // and returns unique id as response.
    public UUID processReciept(Reciept reciept) {
        // To calculate points for the reciept payload.
        int points = calculateRecieptPoints(reciept);
        UUID id = UUID.randomUUID();

        // To store id and points as key and value.
        pointsCache.addReceiptIdAndPoints(id, points);
        return id;
    }

    // To fetch and return points associated with reciept id.
    public int getPointsForReceiptId(UUID id) {
        return pointsCache.getPointsForReceiptUuid(id);
    }

    // To calulate reciept points
    private int calculateRecieptPoints(Reciept reciept) {
        int points = 0;

        // One point for every alphanumeric character in the retailer name.
        points += reciept.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
        // 50 points if the total is a round dollar amount with no cents.
        double total = reciept.getTotal();
        if (total == Math.floor(total)) {
            points += 50;
        }
        // 25 points if the total is a multiple of 0.25.
        if (total % 0.25 == 0) {
            points += 25;
        }
        // 5 points for every two items on the receipt.
        points += (reciept.getItems().size() / 2) * 5;
        // If the trimmed length of the item description is a multiple of 3, multiply
        // the price by 0.2 and round up to the nearest integer. The result is the
        // number of points earned.
        for (var item : reciept.getItems()) {
            if (item.getShortDescription().trim().length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                points += Math.ceil(price * 0.2);
            }
        }
        // 6 points if the day in the purchase date is odd.
        LocalDate date = reciept.getPurchaseDate();
        if (date.getDayOfMonth() % 2 != 0) {
            points += 6;
        }
        // 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        LocalTime time = reciept.getPurchaseTime();
        if ((time.getHour() > 14 && time.getHour() < 16) || (time.getHour() == 14 && time.getMinute() > 0)) {
            points += 10;
        }
        return points;
    }
}
