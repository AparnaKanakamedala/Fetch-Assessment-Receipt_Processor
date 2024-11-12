package com.Fetch.ReceiptProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.Fetch.ReceiptProcessor.model.Item;
import com.Fetch.ReceiptProcessor.model.Reciept;
import com.Fetch.ReceiptProcessor.service.FetchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(FetchController.class)
public class FetchControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;

        Reciept testRecieptWithValidData;

        @MockBean
        FetchService fetchService;

        @BeforeEach
        void setup() {
                testRecieptWithValidData = new Reciept();
                testRecieptWithValidData.setRetailer("Target");
                testRecieptWithValidData.setPurchaseDate(LocalDate.parse("2022-01-01"));
                testRecieptWithValidData.setPurchaseTime(LocalTime.parse("13:01"));
                testRecieptWithValidData.setTotal(35.35);

                testRecieptWithValidData.setItems(Arrays.asList(
                                new Item("Mountain Dew 12PK", "6.49"),
                                new Item("Emils Cheese Pizza", "12.25")));

        }

        @Test
        void testRecieptProcessReturnsId() throws Exception {
                UUID receiptId = UUID.fromString("7fb1377b-b223-49d9-a31a-5a02701dd310");
                when(fetchService.processReciept(any(Reciept.class))).thenReturn(receiptId);

                // Convert the receipt to JSON format
                String receiptJson = objectMapper.writeValueAsString(testRecieptWithValidData);

                // Perform the POST request
                mockMvc.perform(post("/reciepts/process")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(receiptJson))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(receiptId.toString()));
        }

        @Test
        void testRecieptProcessWithInvalidPayload() throws Exception {

        }

        @Test
        void testGetRecieptPointsReturnsValidPoints() throws Exception {

                String recieptID = "7fb1377b-b223-49d9-a31a-5a02701dd310";
                int points = 32;
                when(fetchService.getPointsForReceiptId(UUID.fromString(recieptID)))
                                .thenReturn(points);
                mockMvc.perform(get("/reciepts/{id}/points", UUID.fromString(recieptID)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.points").value(points));

        }

        @Test
        void testGetRecieptPointsWhenIdIsNotInCache() throws Exception {
                String invalidRecieptID = "7fb1377b-b223-49d9-a31a-5a02701dd310";
                int points = 0;
                when(fetchService.getPointsForReceiptId(UUID.fromString(invalidRecieptID)))
                                .thenReturn(points);
                mockMvc.perform(get("/reciepts/{id}/points", UUID.fromString(invalidRecieptID)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.points").value(points));
        }
}
