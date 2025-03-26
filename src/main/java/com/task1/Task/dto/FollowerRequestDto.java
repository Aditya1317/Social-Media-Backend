package com.task1.Task.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerRequestDto {

    private Long id;

    private Long requestToUserId;
    private Long requestByUserId;
    private String status;
    private LocalDateTime requestDate;
}
