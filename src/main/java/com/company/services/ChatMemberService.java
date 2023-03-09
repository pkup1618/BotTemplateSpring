package com.company.services;


import com.company.data.ChatMember;
import com.company.repositories.ChatMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис (обёртка над репозиторием) для всех взаимодействий с базой данных
 */
@Service
public class ChatMemberService
{
    ChatMemberRepository chatMemberRepository;

    @Autowired
    public void setChatMemberRepository(ChatMemberRepository chatMemberRepository)
    {
        this.chatMemberRepository = chatMemberRepository;
    }

    /**
     * Метод установки локализации
     * @param id - id пользователя
     * @param locale - локаль, которую надо установить
     */
    @Transactional
    public void changeLocale(Long id, String locale)
    {
        {
            ChatMember chatMember = chatMemberRepository.getReferenceById(id);
            chatMember.setLocale(locale);
            chatMemberRepository.save(chatMember);
        }
    }

    /**
     * Метод для добавления пользователя в базу данных
     * (По умолчанию добавляются пользователи с русской локализацией)
     * @param chatMember - пользователь, id нужно установить самостоятельно, взяв в telegram
     */
    @Transactional
    public void save(ChatMember chatMember)
    {
        if (!chatMemberRepository.existsById(chatMember.getId()))
        {
            chatMember.setLocale("ru");
            chatMemberRepository.save(chatMember);
        }
    }

    /**
     * Метод для поиска пользователя в базе данных
     * @param id - id пользователя
     */
    public ChatMember findById(Long id)
    {
        return chatMemberRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("Такая запись отсутствует в базе данных"));
    }
}
