package com.company.services;


import com.company.data.ChatMember;
import com.company.repositories.ChatMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMemberService
{
    ChatMemberRepository chatMemberRepository;
    @Autowired
    public void setChatMemberRepository(ChatMemberRepository chatMemberRepository)
    {
        this.chatMemberRepository = chatMemberRepository;
    }

    //todo проверки
    public void save(ChatMember chatMember)
    {
        chatMemberRepository.save(chatMember);
    }

    public void save(Long id)
    {
        ChatMember chatMember = new ChatMember(id);
        chatMemberRepository.save(chatMember);
    }
}
