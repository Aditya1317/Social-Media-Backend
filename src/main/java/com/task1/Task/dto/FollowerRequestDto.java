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
    private Long request_to_user_id;
    private Long request_by_user_id;
    private String status;
    private LocalDateTime request_date;
}
