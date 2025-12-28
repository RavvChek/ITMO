from flask import Flask, render_template_string
import os
import redis
import logging
import socket

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

REDIS_HOST = os.getenv("REDIS_HOST", "redis")
REDIS_PORT = int(os.getenv("REDIS_PORT", 6379))
REDIS_KEY = os.getenv("REDIS_KEY", "hits")

CONTAINER_NAME = socket.gethostname()

try:
    r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)
    r.ping()
    app.logger.info(f"Connected to Redis at {REDIS_HOST}:{REDIS_PORT}")
except Exception as e:
    app.logger.warning(f"Cannot connect to Redis at {REDIS_HOST}:{REDIS_PORT}: {e}")
    r = None

TEMPLATE = """
<!doctype html>
<html>
  <head><meta charset="utf-8"><title>Мини-магазин — счётчик</title></head>
  <body style="font-family: Arial, sans-serif; margin: 2rem; background-color: #1e1e1e; color: #f0f0f0;">
    <h1>Мини-магазин — счётчик посещений</h1>
    <p style="font-size: 2rem;">Счётчик: <strong>{{ count }}</strong></p>
    <p style="font-size: 1.2rem;">
      Запрос обработан контейнером: <strong>{{ container_name }}</strong>
    </p>
    <div style="background-color: #2e2e2e; padding: 1rem; border-radius: 5px; margin-top: 1rem;">
      <p><strong>Статус Redis:</strong> {{ redis_status }}</p>
      <p><strong>Хост Redis:</strong> {{ redis_host }}:{{ redis_port }}</p>
    </div>
  </body>
</html>
"""

@app.route("/")
def index():
    if r:
        try:
            count = r.incr(REDIS_KEY)
            redis_status = "OK"
        except Exception as e:
            count = "ошибка"
            redis_status = f"Ошибка: {e}"
    else:
        count = "недоступен"
        redis_status = "Недоступен"

    return render_template_string(
        TEMPLATE,
        count=count,
        container_name=CONTAINER_NAME,
        redis_status=redis_status,
        redis_host=REDIS_HOST,
        redis_port=REDIS_PORT
    )

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=int(os.getenv("PORT", 5000)))
