FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY simulator.py .
COPY rabbitmq.py .
COPY configuration.ini .

CMD ["python", "simulator.py"]
