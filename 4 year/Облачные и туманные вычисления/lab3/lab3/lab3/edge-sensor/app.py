import json
import os
import random
import time
from datetime import datetime, timezone

import paho.mqtt.client as mqtt


def env_int(name: str, default: int) -> int:
    try:
        return int(os.getenv(name, str(default)))
    except ValueError:
        return default


def env_float(name: str, default: float) -> float:
    try:
        return float(os.getenv(name, str(default)))
    except ValueError:
        return default


MQTT_HOST = os.getenv("MQTT_HOST", "localhost")
MQTT_PORT = env_int("MQTT_PORT", 1883)
MQTT_TOPIC = os.getenv("MQTT_TOPIC", "factory/temperature")
DEVICE_ID = os.getenv("DEVICE_ID", "sensor-1")

INTERVAL_SECONDS = env_float("INTERVAL_SECONDS", 1.0)
TEMP_MIN = env_float("TEMP_MIN", 55.0)
TEMP_MAX = env_float("TEMP_MAX", 95.0)


def make_temperature() -> float:
    if random.random() < 0.15:
        return random.uniform(81.0, TEMP_MAX)
    return random.uniform(TEMP_MIN, 79.5)


def main() -> None:
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
    client.connect(MQTT_HOST, MQTT_PORT, keepalive=60)
    client.loop_start()

    print(
        f"[edge-sensor] Publishing to mqtt://{MQTT_HOST}:{MQTT_PORT} topic={MQTT_TOPIC} "
        f"device_id={DEVICE_ID} interval={INTERVAL_SECONDS}s"
    )

    try:
        while True:
            temperature = round(make_temperature(), 2)
            payload = {
                "device_id": DEVICE_ID,
                "temperature": temperature,
                "ts": datetime.now(timezone.utc).isoformat(),
            }
            msg = json.dumps(payload, ensure_ascii=False)
            result = client.publish(MQTT_TOPIC, msg, qos=0, retain=False)
            status = "ok" if result.rc == mqtt.MQTT_ERR_SUCCESS else f"rc={result.rc}"
            print(f"[edge-sensor] {status} {msg}")
            time.sleep(INTERVAL_SECONDS)
    except KeyboardInterrupt:
        pass
    finally:
        client.loop_stop()
        client.disconnect()


if __name__ == "__main__":
    main()