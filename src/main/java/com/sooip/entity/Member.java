package com.sooip.entity;

import com.sooip.constant.Role;
import com.sooip.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Name;

    @Column(unique = true)
    private String email;

    private String password;

    private String postNumber;
    private String address;
    private String addressDetail;
    private String addressInfo;

    private String phone1;
    private String phone2;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());

        member.setPostNumber(memberFormDto.getPostNumber());
        member.setAddress(memberFormDto.getAddress());
        member.setAddressDetail(memberFormDto.getAddressDetail());
        member.setAddressInfo(memberFormDto.getAddressInfo());

        member.setPhone1(memberFormDto.getPhone1());
        member.setPhone2(memberFormDto.getPhone2());

        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
