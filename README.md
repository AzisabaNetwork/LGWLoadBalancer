# LGWLoadBalancer
プレイヤーを自動で複数のサーバーに振り分けます。

## 使用上の注意
初回起動時は、設定ファイルを生成して自動でプラグインが無効化されます。\
設定ファイルの編集が完了してから、プラグインを有効化してください。

## コマンド
- /lgwlb go (perm: `lgwloadbalancer.cmd.lgwlb.go`)
  - 実行者のプレイヤーを自動で振り分けて転送させます。
- /lgwlb stat (perm: `lgwloadbalancer.cmd.lgwlb.stat`)
  - しきい値やサーバーごとのプレイヤー数を確認することができます。

## 振り分けの仕組み
1. サーバーのプレイヤー数を一定間隔ごとに取得
    - config内に登録されたサーバーが対象
    - 5秒間隔で実行
2. サーバーのプレイヤー数を比較
    - configに書かれた順番で行われる
3. もし、しきい値を超えていなければそのサーバーにプレイヤーを転送
    - playerThresholdの値がこのしきい値になります。

## 設定ファイル
```yml
version: 1
playerThreshold: 10
servers:
- "serverA"
- "serverB"
```
- version: 設定ファイルのバージョンのため、変更しないでください。
- playerThreshold: 1サーバー当たりのプレイヤー数のしきい値です。
- servers: ここに書かれたサーバーが振り分け先として自動で登録されます。
