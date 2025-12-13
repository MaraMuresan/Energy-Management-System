import React, { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import "../index.css";


export default function AdminChat() {
    const [messages, setMessages] = useState([]);
    const [reply, setReply] = useState("");
    const [selectedUser, setSelectedUser] = useState(null);
    const users = [...new Map( messages.map(m => [m.userId, m])).values()];
    const connectedRef = useRef(false);

    const stompClient = useRef(null);

    useEffect(() => {
        if (connectedRef.current) return;
        connectedRef.current = true;

        const token = localStorage.getItem("token");
        const socket = new SockJS(
            "http://localhost/websocket?token=" + token
        );
        const stomp = Stomp.over(socket);
        stomp.debug = () => {};

        stomp.connect(
            { Authorization: "Bearer " + token },
            () => {
                stomp.subscribe("/topic/chat/admin", (frame) => {
                    const msg = JSON.parse(frame.body);
                    setMessages(prev => [...prev, msg]);
                });

                stompClient.current = stomp;
            }
        );
    }, []);

    const sendReply = () => {
        if (!reply.trim() || !selectedUser) return;

        const lastUserMessage = messages.find(
            m => m.userId === selectedUser && m.username
        );

        stompClient.current.send(
            "/app/chat.adminReply",
            { userId: selectedUser },
            reply
        );

        setMessages(prev => [
            ...prev,
            {
                userId: selectedUser,
                username: lastUserMessage?.username,
                from: "ADMIN",
                content: reply
            }
        ]);

        setReply("");
    };

    return (
        <div className="page-center">
            <div className="card large-card" style={{ width: "70%" }}>
                <h2 className="title">Admin Support Chat</h2>

                <div style={{ display: "flex", gap: "20px" }}>
                    <div style={{ width: "30%", borderRight: "1px solid #ddd" }}>
                        <h3>Users</h3>
                        { users.map((m, i) => (
                            <div
                                key={i}
                                className="user-item"
                                onClick={() => setSelectedUser(m.userId)} >
                                {m.username ? `${m.username} (${m.userId})` : m.userId}
                            </div>
                        ))}
                    </div>

                    <div style={{ flex: 1 }}>
                        {selectedUser ? (
                            <>
                                <h3>Chat with {selectedUser}</h3>

                                <div className="chat-messages" style={{ maxHeight: "300px" }}>
                                    {messages
                                        .filter(m => m.userId === selectedUser)
                                        .map((m, i) => (
                                            <div key={i}>
                                                <b>{m.from}:</b> {m.content}
                                            </div>
                                        ))}
                                </div>

                                <div className="chat-input">
                                    <input
                                        className="input"
                                        value={reply}
                                        onChange={(e) => setReply(e.target.value)}
                                        placeholder="Type reply..."
                                    />
                                    <button className="button" onClick={sendReply}>
                                        Send
                                    </button>
                                </div>
                            </>
                        ) : (
                            <p>Select a user to chat with</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
