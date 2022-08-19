package com.beam.drive.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShareRequest {
    private List<String> users;
}
