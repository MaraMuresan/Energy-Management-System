import React, { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import "../index.css";

export default function UserChatPopup() {
    const [open, setOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");

    const [showOptions, setShowOptions] = useState(false);

    const stompClient = useRef(null);
    const userId = useRef(null);
    const lastSentRef = useRef(null);
    const botReplyReceivedRef = useRef(false);

    function decodeJwt(token) {
        const payload = token.split(".")[1];
        return JSON.parse(atob(payload));
    }

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        const decoded = decodeJwt(token);
        userId.current = decoded.id;

        const socket = new SockJS(
            "http://localhost/websocket?token=" + token
        );
        const stomp = Stomp.over(socket);
        stomp.debug = () => {};

        stomp.connect(
            { Authorization: "Bearer " + token },
            () => {
                stomp.subscribe(`/topic/chat/user/${userId.current}`, (frame) => {
                    const msg = JSON.parse(frame.body);

                    if (msg.from === "BOT") {
                        botReplyReceivedRef.current = true;
                        setShowOptions(false);
                    }

                    addMessage(msg.from, msg.content);
                });

                stompClient.current = stomp;
            }
        );
    }, []);

    const addMessage = (from, content) => {
        setMessages((prev) => [...prev, { from, content }]);
    };

    const send = () => {
        if (!input.trim() || !stompClient.current) return;

        lastSentRef.current = input;
        botReplyReceivedRef.current = false;

        stompClient.current.send("/app/chat.send", {}, input);

        addMessage("You", input);
        setInput("");

        setShowOptions(false);

        setTimeout(() => {
            if (!botReplyReceivedRef.current) {
                setShowOptions(true);
            }
        }, 500);
    };

    const askAI = () => {
        const question = lastSentRef.current;
        stompClient.current.send("/app/chat.aiRequest", {}, question);
        setShowOptions(false);
    };

    const askAdmin = () => {
        const question = lastSentRef.current;
        stompClient.current.send("/app/chat.waitForAdmin", {}, question);
        setShowOptions(false);
    };


    return (
        <>
            <button className="chat-button" onClick={() => setOpen(!open)}>
                Chat
            </button>

            {open && (
                <div className="chat-popup">
                    <div className="chat-header">
                        <b>Support Chat</b>
                        <button className="chat-close" onClick={() => setOpen(false)}>
                            ×
                        </button>
                    </div>

                    <div className="chat-messages">
                        {messages.map((m, i) => (
                            <div key={i} className={`chat-msg ${m.from}`}>
                                <b>{m.from}:</b> {m.content}
                            </div>
                        ))}
                    </div>

                    {showOptions && (
                        <div className="chat-options">
                            <button className="button" onClick={askAI}>Ask AI instantly</button>
                            <button className="button" onClick={askAdmin}>Wait for Admin response</button>
                        </div>
                    )}

                    <div className="chat-input">
                        <input
                            className="input"
                            placeholder="Type message..."
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                        />
                        <button className="button" onClick={send}>Send</button>
                    </div>
                </div>
            )}
        </>
    );
}
