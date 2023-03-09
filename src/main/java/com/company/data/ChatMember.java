package com.company.data;


import jakarta.persistence.*;

@Entity(name = "chat_member")
public class ChatMember
{
    public ChatMember()
    {

    }

    public ChatMember(Long id)
    {
        this.id = id;
    }

    @Id()
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "locale")
    private String locale;

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }
}
