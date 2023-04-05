const express = require('express');
const app = express();
const port = 3000;

app.use(express.json());

app.get('/', (req, res) => {
  res.send('Hello World!')
});

app.post('/login', (req, res) => {
  const { username, password } = req.body;
  console.log('username:', username);
  console.log('password:', password);
  res.send(
    {
      "errorCode":0
    }
  );
});

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send('Something went wrong!');
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
});