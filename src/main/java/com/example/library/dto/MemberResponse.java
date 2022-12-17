package com.example.library.dto;

import com.example.library.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {

    private Long id;
    private String userName;

    private String eMail;


    public MemberResponse(Member m){
        this.id = m.getId();
        this.userName = m.getUserName();
        this.eMail = m.getEMail();
    }

}
