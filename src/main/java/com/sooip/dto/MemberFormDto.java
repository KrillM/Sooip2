package com.sooip.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {
    @NotBlank(message = "이름을 정확히 입력하세요")
    private String Name;

    @NotEmpty(message = "이메일 값을 입력해주세요")
    @Email(message = "이메일 형식이 아닙니다")
    private String email;
    
    @NotEmpty(message = "비밀번호는 필수 입니다")
    @Length(min=6, max=10, message = "비밀번호는 6~10자 사이로 해주세요")
    private String password;

    @NotEmpty
    private String address;
    @NotEmpty(message = "주소를 자세히 적어주세요")
    private String addressDetail;
    @NotEmpty
    private String addressInfo;
    @NotEmpty(message = "우편번호를 입력하세요")
    private String postNumber;

    @NotEmpty(message = "휴대전화 앞자리를 입력해주세요")
    private String phone1;
    @NotEmpty(message = "휴대전화 뒷자리를 입력해주세요")
    private String phone2;
}
