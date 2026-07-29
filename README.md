![Java Version](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)
![Render](https://img.shields.io/badge/Render-Deployed-black?style=for-the-badge&logo=render)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

# 🎮 Game Giveaways & Freebies Telegram Notifier

Un bot autónomo desarrollado en Java con **Spring Boot** que rastrea de forma periódica múltiples plataformas de videojuegos en busca de juegos 100% gratuitos y promociones por tiempo limitado, enviando alertas enriquecidas con HTML e imágenes directamente a un canal o chat privado de **Telegram**.

---

## 🚀 Características Principales

* **Multicanal / Multifuente:** Integra consultas a **GamerPower API**, **CheapShark API** y **FreeToGame API**.
* **Ejecución Autónoma:** Tareas programadas en segundo plano mediante `@Scheduled`.
* **Cero Duplicados:** Mantiene en memoria un registro unificado para evitar avisos repetidos.
* **Respeto de Rate Limits:** Pausas estratégicas para cumplir los límites de la API Bot de Telegram (*HTTP 429 prevention*).
* **Configuración Segura:** Inyección de credenciales mediante variables de entorno (`@Value`).
* **Container Ready:** Incluye soporte para **Docker** y despliegue rápido en la nube (Render / Koyeb).

---

## 🛠️ Tecnologías Utilizadas

* **Java 17**
* **Spring Boot** 
* **RestClient** 
* **Jackson Annotations** 
* **Telegram Bot API**
* **Docker**

---

## 📁 Arquitectura del Proyecto

```text
es.lluentdev.notifier/
├── client/           # Clientes HTTP para APIs externas (GamerPower, CheapShark, FreeToGame, Telegram)
├── controller/       # REST Controller para disparar comprobaciones manuales
├── model/            # DTO Records con Mapeo JSON
├── service/          # Lógica de negocio, Scheduler y control de duplicados
└── NotifierApplication.java
```
## 📱 Vista Previa en Telegram

| Alerta de Juego Gratis |
| :---: |
| <img width="216" height="448" alt="Screenshot_20260729_130052_Telegram" src="https://github.com/user-attachments/assets/ad12d2d7-03a0-4e4d-b183-cd51876acea5" />
 |
