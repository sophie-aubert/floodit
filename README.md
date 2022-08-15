# Flood It

A Flood It web game.

## Setup

```bash
CREATE DATABASE floodit;
CREATE DATABASE floodit_test;
CREATE USER 'floodit'@'%' IDENTIFIED BY '<PASSWORD>';
GRANT ALL ON floodit.* to 'floodit'@'%';
GRANT ALL ON floodit_test.* to 'floodit'@'%';
```
