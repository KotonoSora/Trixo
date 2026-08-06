import wave
import struct
import math
import os

def generate_wave(file_name, frequency=440, duration=0.1, volume=0.5, sample_rate=44100):
    """Generates a simple sine wave sound file."""
    num_samples = int(duration * sample_rate)

    # Ensure directory exists
    os.makedirs(os.path.dirname(file_name), exist_ok=True)

    with wave.open(file_name, 'w') as wav_file:
        # Mono, 2 bytes per sample, sample_rate
        wav_file.setparams((1, 2, sample_rate, num_samples, 'NONE', 'not compressed'))

        for i in range(num_samples):
            # Sine wave formula: volume * sin(2 * pi * frequency * t)
            value = int(volume * 32767.0 * math.sin(2.0 * math.pi * frequency * i / sample_rate))
            data = struct.pack('<h', value)
            wav_file.writeframesraw(data)

def generate_milestone_sound(file_name, start_freq=440, end_freq=880, duration=0.2, volume=0.5, sample_rate=44100):
    """Generates a sliding frequency sound file."""
    num_samples = int(duration * sample_rate)
    os.makedirs(os.path.dirname(file_name), exist_ok=True)

    with wave.open(file_name, 'w') as wav_file:
        wav_file.setparams((1, 2, sample_rate, num_samples, 'NONE', 'not compressed'))

        for i in range(num_samples):
            # Linearly interpolate frequency
            t = i / sample_rate
            current_freq = start_freq + (end_freq - start_freq) * (i / num_samples)
            # Use phase accumulation to avoid frequency artifacts
            # But for simple short beeps, this simplified version works:
            value = int(volume * 32767.0 * math.sin(2.0 * math.pi * current_freq * t))
            data = struct.pack('<h', value)
            wav_file.writeframesraw(data)

def generate_win_sound(file_name, sample_rate=44100):
    """Generates a multi-note win chime."""
    notes = [523.25, 659.25, 783.99, 1046.50] # C5, E5, G5, C6
    duration_per_note = 0.15
    num_samples = int(duration_per_note * len(notes) * sample_rate)
    os.makedirs(os.path.dirname(file_name), exist_ok=True)

    with wave.open(file_name, 'w') as wav_file:
        wav_file.setparams((1, 2, sample_rate, num_samples, 'NONE', 'not compressed'))
        for freq in notes:
            for i in range(int(duration_per_note * sample_rate)):
                value = int(0.5 * 32767.0 * math.sin(2.0 * math.pi * freq * i / sample_rate))
                data = struct.pack('<h', value)
                wav_file.writeframesraw(data)

def generate_lose_sound(file_name, sample_rate=44100):
    """Generates a descending lose sound."""
    notes = [392.00, 349.23, 329.63, 261.63] # G4, F4, E4, C4
    duration_per_note = 0.2
    num_samples = int(duration_per_note * len(notes) * sample_rate)
    os.makedirs(os.path.dirname(file_name), exist_ok=True)

    with wave.open(file_name, 'w') as wav_file:
        wav_file.setparams((1, 2, sample_rate, num_samples, 'NONE', 'not compressed'))
        for freq in notes:
            for i in range(int(duration_per_note * sample_rate)):
                value = int(0.5 * 32767.0 * math.sin(2.0 * math.pi * freq * i / sample_rate))
                data = struct.pack('<h', value)
                wav_file.writeframesraw(data)

if __name__ == "__main__":
    output_dir = "app/src/main/res/raw"

    print("Generating basic sounds...")
    generate_wave(os.path.join(output_dir, "tap.wav"), frequency=600, duration=0.05)
    generate_wave(os.path.join(output_dir, "error.wav"), frequency=150, duration=0.3)

    print("Generating win/lose sounds...")
    generate_win_sound(os.path.join(output_dir, "win.wav"))
    generate_lose_sound(os.path.join(output_dir, "lose.wav"))

    print("Generating milestone sounds...")
    # Milestone sounds (milestone_100, milestone_200, ...)
    for m in [100, 200, 300, 400, 500]:
        generate_milestone_sound(
            os.path.join(output_dir, f"milestone_{m}.wav"),
            start_freq=440 + (m // 100) * 100,
            end_freq=880 + (m // 100) * 100,
            duration=0.3
        )

    print("Done!")
