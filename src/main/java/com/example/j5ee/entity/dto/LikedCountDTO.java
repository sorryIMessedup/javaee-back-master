package com.example.j5ee.entity.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class LikedCountDTO {
    private String key;//用户id的string形式
    private Integer count;
}
