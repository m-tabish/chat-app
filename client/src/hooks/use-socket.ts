'use client';

import { useEffect, useRef, useState, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';

const SOCKET_URL = 'http://localhost:8080/ws';

export interface ChatMessage {
  content: string;
  senderId: string;
  roomId: string;
  sentAt?: string;
}

export function useSocket(roomId: string | null) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [connected, setConnected] = useState(false);
  const stompClient = useRef<Client | null>(null);

  useEffect(() => {
    if (!roomId) return;

    const socket = new SockJS(SOCKET_URL);
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (msg) => console.log('STOMP:', msg),
      onConnect: () => {
        console.log('Connected to WebSocket');
        setConnected(true);
        
        client.subscribe(`/topic/room/${roomId}`, (message: IMessage) => {
          const payload = JSON.parse(message.body);
          setMessages((prev) => [...prev, payload]);
        });
      },
      onDisconnect: () => {
        console.log('Disconnected');
        setConnected(false);
      },
    });

    client.activate();
    stompClient.current = client;

    return () => {
      client.deactivate();
    };
  }, [roomId]);

  const sendMessage = useCallback((message: ChatMessage) => {
    if (stompClient.current?.connected) {
      stompClient.current.publish({
        destination: '/app/chat.sendMessage',
        body: JSON.stringify(message),
      });
    }
  }, []);

  return { messages, setMessages, connected, sendMessage };
}
