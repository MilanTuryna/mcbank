#
# MyBank - A minecraft plugin for complex virtual bank and economy
#
# Developed by MilanT:
#    https://github.com/MilanTuryna
#    https://turyna.eu/
#
# JOIN-EVENT MESSAGE VARIABLES: %player%, %balance%, %actualDeposit%, %actualWithdraw%s
#
# messages.sucessfully_sent : player,donatedplayer,currencysymbol,balance,payamount

prefix: "&8[&6McBank&8]&7"
currencySymbol: "$"
playerPermissions: false
notifyRelations: false
storage:
  active: "yaml"
  mysql:
    db: ""
    name: ""
    password: ""
moneyBag:
  display_name: "§6Moneybag, eyy!"
  item_url: "http://textures.minecraft.net/texture/e36e94f6c34a35465fce4a90f2e25976389eb9709a12273574ff70fd4daa6852"
  lore:
    - "&7Richness of moneybag: &e%amount%currencySymbol%"
    - "&7First owner: &d%firstOwner%"
    - "&8————————————————————————————————————————————————————————"
    - "&7Forward richness of moneybag to §byour bank account!"
    - "&8————————————————————————————————————————————————————————"
    - "&aUse &lRIGHT-CLICK &ababy!"

events:
  deathEvent:
    message: ".." # %withdraw%, %deposit%, %player%
    withdraw: 0
    deposit: 0
  killEvent:
    message: ".." # %target%, %withdraw%, %deposit%, %player%
    withdraw: 0
    deposit: 0
  joinEvent:
    message:
      - " "
      - "  &aRegistering §e%player%&7 to our economy.."
      - "  &7Hey, welcome &e%player%&7 to our bank-system! "
      - "  &7Your momentarily account balance is a &a%balance% %currencySymbol%&7."
      - "  &6It's good, but you can do better &c&l❤&6!"
      - " "
      - "  &7Use &f/mcbank&7 for administration of your bank account!"
      - " "
    withdraw: 0
    deposit: 0
  quitEvent:
    withdraw: 0
    deposit: 0
errorMessages:
  invalid_number: "%prefix% &cYou have entered an amount that is not a positive number."
  bigger_amount: "%prefix%  &cYou have entered an amount, which is bigger than total account balance."
  bad_argument: "%prefix% &cBad argument! &7Write &a/mcbank&7 for help with bank account"
  admin_bad_argument: "%prefix% &cBad argument! &7Write &a/adminmcbank&7 for help with bank account"
  no_console: "%prefix%  &cThis command is enabled only for players!"
  pay_to_offline: "%prefix% &cYou want to send money to player which is not online."
  player_account_not_found: "%prefix% &cPlayer's (&e%player%&c) bank account not found"
  no_permission: "%prefix% &cYou don't have permission for bank command."
  reload_not_working: "%prefix% &cAn error occurred when plugin reloading the file configuration. &a>> Check console logs for solution"
messages:
  money_bag_use: "%prefix% &7You successfully forwarded money bag richness (&a%balance%%currencySymbol%&7) to your bank account."
  money_bag_get: "%prefix% &7You have successfully withdrawn &a%withdraw%%currencySymbol%&7 from your bank account."
  status_command: "%prefix% &e%player%&7, your current account balance is &a%balance%%currencySymbol%"
  received_from_player: "%prefix% &7You received &a%payAmount%&7 from &e%player%"
  successfully_sent: "%prefix% &7You successfully sent &c%payAmount%%currencySymbol%&7 to &e%donatedPlayer%&7, and now you have &a%balance%"
  pm_thanks_to_sponsor: "%prefix% "
  bc_thanks_to_sponsor: "%prefix% "
  admin_configuration_reloaded: "%prefix% &7You successfully reloaded &aMcBank configuration!"
  admin_success_remove: "%prefix% &7You successfully removed &a%amount%%currencySymbol%&7 from &e%target%'s bank account" # %target%, %amount%
  admin_success_add: "%prefix% &7You successfully added &a%amount%%currencySymbol%&7 to &e%target%'s bank account" # %target%, %amount%
  admin_notify_reloadconfiguration: "%prefix% &7Administrator &f%administrator% &asuccessfully reloaded plugin configuration."
  admin_notify_addmoney_relation: "%prefix% &7Administrator &f%administrator% &7successfully added &a%amount%%currencySymbol%&7 to &e%target%"
  admin_notify_removemoney_relation: "%prefix% &7Administrator &f%administrator% &7successfully removed &a%amount%%currencySymbol%&7 from &e%target%'s&7 bank account"
  admin_notify_pay_relation: "%prefix% &7Player &e%donator%&7 has sent &a%amount%%currencySymbol%&7 to &e%target%"
  admin_notify_sponsor_relation: "%prefix% &7Player sponsor &e%donator% gifted &a%amount%%currencySymbol% to &7%%server economy."
  admin_configuration_changed: "%prefix% &7You successfully changed &e%key% to &a%value%"
  admin_check_balance: "%prefix% &e%player% §7currently has §a%balance%%currencySymbol%"
