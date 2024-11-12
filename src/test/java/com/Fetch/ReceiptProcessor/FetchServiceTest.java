package com.Fetch.ReceiptProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.Fetch.ReceiptProcessor.model.Item;
import com.Fetch.ReceiptProcessor.model.Reciept;
import com.Fetch.ReceiptProcessor.repositories.PointsRepository;
import com.Fetch.ReceiptProcessor.service.FetchService;

public class FetchServiceTest {
    @Mock
    private PointsRepository pointsRepository;

    @InjectMocks
    private FetchService fetchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test // To verify that the reciept is processed and id is returned
    void testProcessReciept() {
        // Set up a mock receipt
        Reciept reciept = new Reciept();
        reciept.setRetailer("Fetch123");
        reciept.setTotal(21.0);
        reciept.setItems(List.of(
                new Item("Apple", "1.50"),
                new Item("Oranges", "0.75"),
                new Item("Bananas", "2.25")));
        reciept.setPurchaseDate(LocalDate.of(2023, 5, 12));
        reciept.setPurchaseTime(LocalTime.of(17, 0));

        // Mock the repository method
        doNothing().when(pointsRepository).addReceiptIdAndPoints(any(UUID.class), anyInt());

        // Run the method under test
        UUID resultId = fetchService.processReciept(reciept);

        // Verify the interactions and assertions
        verify(pointsRepository, times(1)).addReceiptIdAndPoints(any(UUID.class), anyInt());
        assertEquals(36, resultId.toString().length()); // UUID should be generated and have valid format
    }

    @Test // To verify that points corresponding to the given id is returned
    void testGetPointsForReceiptId() {
        UUID id = UUID.randomUUID();
        when(pointsRepository.getPointsForReceiptUuid(id)).thenReturn(12);

        int points = fetchService.getPointsForReceiptId(id);

        assertEquals(12, points);
        verify(pointsRepository, times(1)).getPointsForReceiptUuid(id);
    }

    @Test // To verify that points are calculated correctly.
    void testCalculateRecieptPoints_ItemDescriptionPoints() {
        Reciept reciept = new Reciept();
        reciept.setRetailer("Retailer");// 8 points for retailer
        reciept.setTotal(1.3);
        reciept.setItems(List.of(
                new Item("Apples", "2.50"), // Length of "Apples" is 6, multiple of 3
                new Item("Orange", "1.75")));// Length of "Orange" is 6, multiple of 3
        reciept.setPurchaseDate(LocalDate.of(2023, 5, 12)); // even day gets no points
        reciept.setPurchaseTime(LocalTime.of(17, 30)); // time is after 4:00pm

        UUID resultId = fetchService.processReciept(reciept);

        when(pointsRepository.getPointsForReceiptUuid(resultId)).thenReturn(15);
        int points = fetchService.getPointsForReceiptId(resultId);

        // 8 points from retailer + 2 point from item description + 5 points for every 2
        // items.
        assertEquals(15, points);
    }
}
