from rabbitmq import RabbitMQ
import json
import random
import time
from datetime import datetime, timedelta
import configparser

configuration = configparser.ConfigParser()
configuration.read("configuration.ini")

DEVICE_ID = configuration["simulator"]["device_id"]
REAL_TIME = configuration["simulator"].getboolean("real_time")

def generate_value(hour):
    if 0 <= hour < 6:
        return random.uniform(1, 3)
    elif 6 <= hour < 9:
        return random.uniform(4, 8)
    elif 9 <= hour < 16:
        return random.uniform(1, 2)
    elif 16 <= hour < 22:
        return random.uniform(6, 10)
    else:
        return random.uniform(2, 4)

def main():
    rabbitmq = RabbitMQ()
    queue = "data-collection-broker"

    current = datetime.now()

    current_value = 0

    while True:
        current_value += generate_value(current.hour) 
        msg = {
            "timestamp": current.isoformat(),
            "device_id": DEVICE_ID,
            "measurement_value": round(current_value, 3)
        }

        rabbitmq.publish(queue, json.dumps(msg))
        print(f"[device={DEVICE_ID}] Sent:", msg)

        current += timedelta(minutes=10)

        time.sleep(600 if REAL_TIME else 1)

main()