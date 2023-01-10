# SealedTrain-Server
**Sealed Train** is a PoC developed in order to prove the importance of cryptography when using QR Codes as a badge for a service. 

Sealed Train has a Client-Server architecture: 

- The server has been developed using Ktor, a Kotlin framework for asynchronus applications$^1$. It exposes REST APIs and uses JSON Web Token for authentication.
    - **JSON Web Token (JWT)** is an open standard ([RFC 7519](https://tools.ietf.org/html/rfc7519)) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed, in our case it is signed with a secret with HMAC alghoritm.$^2$
    - The server connects to a Mongo DB for data storage.
        - The password of the users are hashed (SHA256) with a salt (32 bytes vector generated with SecuredRandom from java.util.random library)
        - Every ticket has a QR Code that is the result of an hash (SHA256) of the infos of the ticket itself.
    - Every time the client makes the request for tickets, they are encrypted with **AES256** algorithm before being sent.
        - The keys are stored on a local **KeyStore**, protected by a password. Each key is protected with the user’s password . Each ticket is encrypted with a different key.
    - When the server receives a request to validate a ticket, it identifies the ticket id and uses the KeyStore to get the Key and decrypt the QR Code. Subsequently, the server looks for the decrypted QrCode inside the DB and , if found, checks its validity.
