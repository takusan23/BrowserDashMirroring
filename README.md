# BrowserDashMirroing

`JavaScript`の`getDisplayMedia`、`MediaRecorder`を使ってミラーリングし`WebM`を生成してサーバーに送って、  
他のブラウザへは`MPEG-DASH`でライブ配信を行いミラーリングを視聴する。

![Imgur](https://imgur.com/dRvd2Rg.png)

## 使いかた
- IDEAでこのプロジェクトを開く
- main関数を起動する
- localhost:8080 を開き、配信開始を押す
- 数秒後、もう一つ localhost:8080 を開き、視聴開始を押す