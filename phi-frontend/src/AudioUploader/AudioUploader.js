import { useState } from "react";

export default function AudioUploader() {
    const [file, setFile] = useState(null);

    const handleFileChange = e => setFile(e.target.files[0]);

    const handleUpload = async () => {
        if (!file) return alert("Select an audio file first");

        const formData = new FormData();
        formData.append("audio", file);

        // this will be reverse proxied later on once Ashutosh can get us the hosting stuff
        const res = await fetch("http://localhost:3001/upload", {
            method: "POST",
            body: formData,
        });

        const data = await res.json();
        console.log("Server response:", data);
    };

    return (
        <div className="p-4">
        <input type="file" accept="audio/*" onChange={handleFileChange} />
        <button onClick={handleUpload}>Upload</button>
        </div>
    );
}
