'use client';

import { useState, useEffect, useMemo } from 'react';
import { useSocket, ChatMessage } from '@/hooks/use-socket';
import { api, ENDPOINTS } from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from '@/components/ui/card';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { LogOut, Send, Plus, Hash, User as UserIcon, Loader2 } from 'lucide-react';
import { format } from 'date-fns';

interface User {
  userId: string;
  name: string;
  email: string;
}

interface Room {
  roomId: string;
  roomName: string;
}

export default function ChatApp() {
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [rooms, setRooms] = useState<Room[]>([]);
  const [activeRoom, setActiveRoom] = useState<Room | null>(null);
  const [inputMessage, setInputMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  // Login State
  const [loginEmail, setLoginEmail] = useState('');
  const [loginName, setLoginName] = useState('');

  const { messages, setMessages, connected, sendMessage } = useSocket(activeRoom?.roomId || null);

  // Persistence
  useEffect(() => {
    const savedUser = localStorage.getItem('chat_user');
    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    try {
      const res = await api.get(`${ENDPOINTS.ROOMS}`);
      setRooms(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error('Failed to fetch rooms', err);
    }
  };

  const fetchMessages = async (roomId: string) => {
    try {
      const res = await api.get(`${ENDPOINTS.MESSAGES}/${roomId}`);
      setMessages(res.data);
    } catch (err) {
      console.error('Failed to fetch messages', err);
    }
  };

  useEffect(() => {
    if (activeRoom) {
      fetchMessages(activeRoom.roomId);
    }
  }, [activeRoom]);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      // For MVP, we'll try to find user by email or create if not exists
      // Backend UserController doesn't have a "get by email", only list or ID
      const usersRes = await api.get(`${ENDPOINTS.USERS}/`);
      let user = usersRes.data.find((u: User) => u.email === loginEmail);
      
      if (!user) {
        const createRes = await api.post(`${ENDPOINTS.USERS}/add`, {
          name: loginName,
          email: loginEmail,
          password: 'password123', // Dummy for now
          userType: 'PUBLIC'
        });
        user = createRes.data;
      }
      
      setCurrentUser(user);
      localStorage.setItem('chat_user', JSON.stringify(user));
    } catch (err) {
      console.error('Login failed', err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('chat_user');
    setActiveRoom(null);
  };

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputMessage.trim() || !currentUser || !activeRoom) return;

    const msg: ChatMessage = {
      content: inputMessage,
      senderId: currentUser.userId,
      roomId: activeRoom.roomId,
    };

    sendMessage(msg);
    setInputMessage('');
  };

  const handleCreateRoom = async (e: React.FormEvent) => {
    e.preventDefault();
    const form = e.target as HTMLFormElement;
    const roomName = (form.elements.namedItem('roomName') as HTMLInputElement).value;
    
    try {
      const res = await api.post(`${ENDPOINTS.ROOMS}/create`, {
        roomName,
        adminId: currentUser?.userId
      });
      setRooms([...rooms, res.data]);
    } catch (err) {
      console.error('Failed to create room', err);
    }
  };

  if (!currentUser) {
    return (
      <div className="flex min-h-screen items-center justify-center p-4 bg-background">
        <Card className="w-full max-w-md border-2">
          <CardHeader className="text-center">
            <CardTitle className="text-2xl font-bold">Welcome to Lore</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleLogin} className="space-y-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">Name</label>
                <Input 
                  placeholder="Enter your name" 
                  value={loginName}
                  onChange={(e) => setLoginName(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <label className="text-sm font-medium">Email</label>
                <Input 
                  type="email" 
                  placeholder="Enter your email" 
                  value={loginEmail}
                  onChange={(e) => setLoginEmail(e.target.value)}
                  required
                />
              </div>
              <Button type="submit" className="w-full" disabled={isLoading}>
                {isLoading ? <Loader2 className="animate-spin" /> : 'Join Chat'}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-background overflow-hidden">
      {/* Sidebar */}
      <div className="w-64 border-r flex flex-col bg-muted/30">
        <div className="p-4 border-b flex items-center justify-between">
          <h1 className="font-bold text-xl flex items-center gap-2">
             Lore
          </h1>
          <Button variant="ghost" size="icon" onClick={handleLogout}>
            <LogOut className="h-4 w-4 text-destructive" />
          </Button>
        </div>
        
        <div className="p-4 flex items-center gap-3 bg-muted/50">
          <Avatar className="h-8 w-8">
            <AvatarFallback>{currentUser.name.charAt(0).toUpperCase()}</AvatarFallback>
          </Avatar>
          <div className="flex-1 overflow-hidden">
            <p className="text-sm font-medium truncate">{currentUser.name}</p>
            <p className="text-xs text-muted-foreground truncate">{currentUser.email}</p>
          </div>
        </div>

        <ScrollArea className="flex-1">
          <div className="p-2 space-y-1">
            <div className="px-3 py-2 text-xs font-semibold text-muted-foreground uppercase flex items-center justify-between">
              Rooms
              <Dialog>
                <DialogTrigger asChild>
                  <Button variant="ghost" size="icon" className="h-4 w-4">
                    <Plus className="h-3 w-3" />
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Create New Room</DialogTitle>
                  </DialogHeader>
                  <form onSubmit={handleCreateRoom} className="space-y-4 pt-4">
                    <Input name="roomName" placeholder="Room Name" required />
                    <Button type="submit" className="w-full">Create</Button>
                  </form>
                </DialogContent>
              </Dialog>
            </div>
            {rooms.map((room) => (
              <Button
                key={room.roomId}
                variant={activeRoom?.roomId === room.roomId ? 'secondary' : 'ghost'}
                className="w-full justify-start gap-2 font-normal"
                onClick={() => setActiveRoom(room)}
              >
                <Hash className="h-4 w-4 opacity-50" />
                {room.roomName}
              </Button>
            ))}
          </div>
        </ScrollArea>
      </div>

      {/* Main Chat Area */}
      <div className="flex-1 flex flex-col min-w-0">
        {activeRoom ? (
          <>
            <div className="p-4 border-b flex items-center justify-between bg-background">
              <div className="flex items-center gap-2">
                <Hash className="h-5 w-5 text-muted-foreground" />
                <h2 className="font-bold">{activeRoom.roomName}</h2>
                <div className={`h-2 w-2 rounded-full ${connected ? 'bg-green-500' : 'bg-red-500'}`} />
              </div>
            </div>

            <ScrollArea className="flex-1 p-4">
              <div className="space-y-4">
                {messages.map((msg, i) => {
                  const isMe = msg.senderId === currentUser.userId;
                  return (
                    <div key={i} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                      <div className={`max-w-[80%] flex items-end gap-2 ${isMe ? 'flex-row-reverse' : 'flex-row'}`}>
                        <Avatar className="h-8 w-8 shrink-0">
                          <AvatarFallback>{msg.senderId.slice(0, 2).toUpperCase()}</AvatarFallback>
                        </Avatar>
                        <div className={`p-3 rounded-2xl ${isMe ? 'bg-primary text-primary-foreground rounded-tr-none' : 'bg-muted rounded-tl-none'}`}>
                          <p className="text-sm leading-relaxed">{msg.content}</p>
                          <span className="text-[10px] opacity-70 mt-1 block">
                            {msg.sentAt ? format(new Date(msg.sentAt), 'HH:mm') : 'Just now'}
                          </span>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            </ScrollArea>

            <div className="p-4 bg-background">
              <form onSubmit={handleSendMessage} className="flex gap-2">
                <Input 
                  placeholder={`Message #${activeRoom.roomName}`} 
                  value={inputMessage}
                  onChange={(e) => setInputMessage(e.target.value)}
                  className="flex-1"
                />
                <Button type="submit" size="icon" disabled={!inputMessage.trim()}>
                  <Send className="h-4 w-4" />
                </Button>
              </form>
            </div>
          </>
        ) : (
          <div className="flex-1 flex items-center justify-center flex-col text-muted-foreground space-y-4">
            <div className="p-4 rounded-full bg-muted">
               <Hash className="h-12 w-12" />
            </div>
            <div className="text-center">
              <h3 className="text-lg font-bold text-foreground">Select a Room</h3>
              <p className="text-sm">Choose a room from the sidebar to start chatting</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
