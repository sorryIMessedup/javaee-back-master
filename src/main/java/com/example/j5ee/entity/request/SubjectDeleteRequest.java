package com.example.j5ee.entity.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubjectDeleteRequest implements Serializable {
    private static final long serialVersionUID = -4775574971951539698L;

    private int sid;
}
