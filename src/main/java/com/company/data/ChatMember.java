package com.company.data;


import jakarta.persistence.*;

@Entity(name = "chat_member")
public class ChatMember
{
    public ChatMember()
    {

    }

    public ChatMember(Long tgId)
    {
        this.tgId = tgId;
    }

    @Id()
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    public Long getId()
    {
        return id;
    }
    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "tgId", nullable = false)
    private Long tgId;
    public Long getTgId()
    {
        return tgId;
    }
    public void setTgId(Long tgId)
    {
        this.tgId = tgId;
    }
}
