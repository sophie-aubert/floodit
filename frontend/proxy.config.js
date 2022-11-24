const frontendProxyPort = process.env.FLOODIT_FRONTEND_API_PROXY_PORT ?? 5000;

module.exports = {
  '/api/**': {
    target: `http://localhost:${frontendProxyPort}`,
    secure: false,
    logLevel: 'debug'
  }
};
