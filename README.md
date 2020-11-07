# McBank
A minecraft plugin for adding bank and economy system to minecraft spigot servers. The plugin has VaultAPI support, but also working without Vault plugin. McBank plugin is working on two storages: MySQL and YAML, default storage is local YAML files.

### Plugin dependencies
- [VaultAPI](https://github.com/MilkBowl/VaultAPI) (soft dependency)

### Plugin configuration
```yaml
prefix: ""
currencySymbol: "$"
playerPermissions: false
notifyRelations: false
storage:
  active: "yaml"
  mysql:
    db: ""
    name: ""
    password: ""
events:
  joinEvent:
    message: "Hello %player%! amount: %balance%, deposit: %actualDeposit%, withdraw: %actualWithdraw%"
    withdraw: 0
    deposit: 0
  quitEvent:
    withdraw: 0
    deposit: 0
errorMessages:
  invalid_number: "&cYou have entered an amount that is not a number."
  bigger_amount: "&cYou have entered an amount, which is bigger than your total account balance."
  bad_argument: "&cBad argument! &7Write &a/mcbank&7 for help with bank account"
  no_console: "&cThis command is enabled only for players!"
  pay_to_offline: "&cYou want to send money to player which is not online."
  no_permission: "&&You don't have permission for bank command."
messages:
  status_command: "&e%player%&7, your current account balance is &a%balance%%currencySymbol%"
  received_from_player: "&7You received &a%payAmount%&7 from &e%player%"
  successfully_sent: "&7You successfully sent &c%payAmount%%currencySymbol%&7 to &e%donatedPlayer%&7, and now you have &a%balance%"
  pm_thanks_to_sponsor: ""
  bc_thanks_to_sponsor: ""
```