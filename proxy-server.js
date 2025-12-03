const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();

app.use('/notes', createProxyMiddleware({
  target: 'https://notesapps-b0bqb4degeekb6cn.japanwest-01.azurewebsites.net',
  changeOrigin: true,
  pathRewrite: { '^/notes': '/notes' },
  onProxyRes: function (proxyRes, req, res) {
    proxyRes.headers['Access-Control-Allow-Origin'] = '*';
  }
}));

app.listen(8080, () => {
  console.log('Proxy server running on http://localhost:8080');
});
