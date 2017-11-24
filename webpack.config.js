const path = require('path');
const root = path.resolve(__dirname, 'src/main/webapp');
const src = path.resolve(root, 'javascript');
const dest = path.resolve(__dirname, 'src/main/webapp/dist');


module.exports = {
    entry: src + '/index.jsx',
    output: {
        filename: 'bundle.js',
        path: dest
    },

    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude: /(node_modules)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['react','stage-2']
                    }
                }
            }
        ]
    }
};