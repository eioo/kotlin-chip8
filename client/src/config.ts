export default {
  host: process.env.HOST || 'localhost',
  port: parseInt(process.env.PORT || '', 10) || 8080,
};
