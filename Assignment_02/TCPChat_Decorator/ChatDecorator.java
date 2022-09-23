import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

public class ChatDecorator implements Chat {

	protected Chat chat;
	
	public ChatDecorator(Chat c) {
		this.chat = c;
	}
		
	@Override
	public void Assemble() {
		this.chat.Assemble();
	}

}
