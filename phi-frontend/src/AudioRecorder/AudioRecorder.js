import { useState, useRef } from "react";
import audioBufferToWav from "audiobuffer-to-wav";

export default function AudioRecorder() {
  const [recording, setRecording] = useState(false);
  const [audioBlob, setAudioBlob] = useState(null);
  const mediaRecorderRef = useRef(null);
  const chunksRef = useRef([]);

  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      mediaRecorderRef.current = new MediaRecorder(stream);
      chunksRef.current = [];

      mediaRecorderRef.current.ondataavailable = (e) => {
        if (e.data.size > 0) {
          chunksRef.current.push(e.data);
        }
      };
      
      mediaRecorderRef.current.onstop = async () => {
        // a weird extension .ogx is created by default
        const blob = new Blob(chunksRef.current, { type: "audio/webm" });
        const arrayBuffer = await blob.arrayBuffer();

        // decode the audio data into an AudioBuffer
        const audioContext = new (window.AudioContext || window.webkitAudioContext)();
        const audioBuffer = await audioContext.decodeAudioData(arrayBuffer);

        // convert AudioBuffer to WAV format at here
        const wavBuffer = audioBufferToWav(audioBuffer);
        const wavBlob = new Blob([new DataView(wavBuffer)], { type: "audio/wav" });

        setAudioBlob(wavBlob);
      };

      mediaRecorderRef.current.start();
      setRecording(true);
    } 
    
    catch (error) {
      console.error("Error accessing microphone:", error);
      alert("Microphone access denied or not available.");
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && recording) {
      mediaRecorderRef.current.stop();
      setRecording(false);
    }
  };

  return (
    <div className="recording-container">
      <button onClick={recording ? stopRecording : startRecording}>
        {recording ? "Stop Recording" : "Start Recording"}
      </button>

      {audioBlob && (
        <div>
          <audio controls src={URL.createObjectURL(audioBlob)} />
          <br />
          <a
            href={URL.createObjectURL(audioBlob)}
            download="recording.wav"
            className="download-link"
          >
            Download recording
          </a>
        </div>
      )}
    </div>
  );
}
