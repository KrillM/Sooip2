package com.sooip.service;

import com.sooip.dto.MemberFormDto;
import com.sooip.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("윤석열");
        memberFormDto.setAddress("서울시 용산구");
        memberFormDto.setAddressDetail("국방부청사");
        memberFormDto.setAddressInfo("무당이당");
        memberFormDto.setPostNumber("12345");
        memberFormDto.setPhone1("1960");
        memberFormDto.setPhone2("1218");
        memberFormDto.setPassword("20222027");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("Register Test")
    public void saveMemberTest(){
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getName(),saveMember.getName());
        assertEquals(member.getAddress(),saveMember.getAddress());
        assertEquals(member.getAddressDetail(),saveMember.getAddressDetail());
        assertEquals(member.getAddressInfo(),saveMember.getAddressInfo());
        assertEquals(member.getPostNumber(),saveMember.getPostNumber());
        assertEquals(member.getPhone1(),saveMember.getPhone1());
        assertEquals(member.getPhone2(),saveMember.getPhone2());
        assertEquals(member.getPassword(),saveMember.getPassword());
    }

    @Test
    @DisplayName("Check overlapped member test")
    public void SaveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e =assertThrows(IllegalStateException.class,() ->{
            memberService.saveMember(member2);});
        assertEquals("이미 가입된 회원입니다", e.getMessage());
    }
}