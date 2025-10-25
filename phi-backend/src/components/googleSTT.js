/* 
 * Doc: https://cloud.google.com/speech-to-text/docs/sync-recognize
 *
 * Doc: real-time speech recognition
 *      (https://cloud.google.com/speech-to-text/docs/samples/speech-transcribe-streaming-mic?hl=en#speech_transcribe_streaming_mic-nodejs)
 */




// Imports the Google Cloud client library
import { readFileSync } from 'fs';
import { SpeechClient } from '@google-cloud/speech';


export async function googleSTT(filename) {
    
    // Creates a client
    const client = new SpeechClient();
    
    /*
     * TODO(developer): Uncomment the following lines before running the sample.
     */
    const encoding = 'LINEAR16';    // 'Encoding of the audio file, e.g. LINEAR16';
    // const sampleRateHertz = 16000;  // Generally recommended Hertz range at the document
    const languageCode = 'en-US'    // 'BCP-47 language code, e.g. en-US';

    const config = {
        encoding: encoding,
        // sampleRateHertz: sampleRateHertz,
        languageCode: languageCode,
    };

    const audio = {
        content: readFileSync(filename).toString('base64'),
    };

    const request = {
        config: config,
        audio: audio,
    };

    // Detects speech in the audio file
    const [response] = await client.recognize(request);
    const transcription = response.results
        .map(result => result.alternatives[0].transcript)
        .join('\n');

    return transcription
}
