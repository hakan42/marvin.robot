let mediaRecorder;
let audioChunks = [];

document.getElementById('startBtn').addEventListener('click', async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    mediaRecorder = new MediaRecorder(stream);
    audioChunks = [];

    mediaRecorder.ondataavailable = (event) => {
      if (event.data.size > 0) {
        audioChunks.push(event.data);
      }
    };

    mediaRecorder.onstop = async () => {
      const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
      const arrayBuffer = await audioBlob.arrayBuffer();
      const byteArray = new Uint8Array(arrayBuffer);

      console.log("Uploading audio...");

      // Post the byte array to the server
      fetch('/speech', {
        method: 'POST',
        headers: { 'Content-Type': 'application/octet-stream' },
        body: byteArray
      })
      .then((response) => {
        if (response.ok) {
          console.log("Audio uploaded successfully!");
        } else {
          console.error("Failed to upload audio.");
        }
      })
      .catch((error) => {
        console.error('Error uploading audio:', error);
      });
    };

    mediaRecorder.start();
    console.log("Recording...");
    document.getElementById('startBtn').disabled = true;
    document.getElementById('stopBtn').disabled = false;
  } catch (error) {
    console.error('Error accessing microphone:', error);
  }
});

document.getElementById('stopBtn').addEventListener('click', () => {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    mediaRecorder.stop();
    console.log("Stopped recording.");
    document.getElementById('startBtn').disabled = false;
    document.getElementById('stopBtn').disabled = true;
  }
});

// Connect to the SSE endpoint and display messages
function startSSE() {
  const messageList = document.getElementById("messages");
  const audioPlayer = document.getElementById("audioPlayer");
  const eventSource = new EventSource("/stream");

  eventSource.onmessage = (event) => {
    const newMessage = document.createElement("li");
    newMessage.className = "message-bubble";
    newMessage.innerHTML = marked.parse(JSON.parse(event.data).content);
    const feedback = JSON.parse(event.data).feedback;
    const sender = JSON.parse(event.data).sender;
    if (feedback) {
      newMessage.style.backgroundColor = "#3c813c";
      newMessage.style.textAlign = "right";
    }
    messageList.appendChild(newMessage);
    messageList.appendChild(document.createElement("div")).style.clear = "both";
    newMessage.scrollIntoView({ behavior: "smooth" });

    const senderHeader = document.createElement("span");
    senderHeader.textContent = sender;
    const hashCode = (str) => {
      let hash = 0;
      for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
      }
      return hash;
    };

    const intToRGB = (i) => {
      const c = (i & 0x00FFFFFF)
      .toString(16)
      .toUpperCase();
      return "00000".substring(0, 6 - c.length) + c;
    };

    senderHeader.style.fontWeight = "bold";
    senderHeader.style.color = `#${intToRGB(hashCode(sender))}`;
    newMessage.insertBefore(senderHeader, newMessage.firstChild);
  }

  // Listen for a custom event indicating chat audio is ready
  eventSource.addEventListener("chatReady", async () => {
    console.log("Chat audio ready! Fetching...");

    // Fetch the chat audio
    try {
      const response = await fetch("/speech");
      if (!response.ok) {
        throw new Error("Failed to fetch audio");
      }

      const blob = await response.blob();
      // Play the audio
      audioPlayer.src = URL.createObjectURL(blob);
      audioPlayer.style.display = "block";
      audioPlayer.play();

      console.log("Playing chat audio...");
    } catch (error) {
      console.error("Error fetching or playing audio:", error);
    }
  });

  eventSource.onerror = () => {
    console.error("Error connecting to SSE stream");
    eventSource.close();
  };
}

// Send a message via POST request
async function sendMessage(event) {
  event.preventDefault();

  const messageInput = document.getElementById("message");
  const message = messageInput.value.trim();

  if (message !== "") {
    try {
      const response = await fetch("/message", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({ message }),
      });

      if (response.ok) {
        console.log("Message sent: " + message);
        messageInput.value = ""; // Clear the input field
      } else {
        console.error("Failed to send message:", response.statusText);
      }
    } catch (error) {
      console.error("Error sending message:", error);
    }
  }
}

// Initialize on page load
window.onload = () => {
  startSSE();

  // Allow form submission with Enter key
  document.getElementById("chatForm").addEventListener("submit", sendMessage);
};