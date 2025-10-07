# ⚔️ StrengthSMP

**StrengthSMP** is a competitive Minecraft Paper plugin inspired by the Strength SMP concept.  
Players gain permanent **Strength buffs** for every player kill — but every death costs them a **life**.  
When a player loses all lives, they **lose all buffs** or can even be **banned or moved to spectator mode**, depending on server configuration.

---

## 🌟 Features

- 💥 **Strength from kills:** Each kill increases the player’s Strength level.
- ❤️ **Lives system:** Every player starts with a set number of lives.
- ☠️ **Death consequences:** Losing all lives removes buffs or bans the player.
- ⚙️ **Configurable behavior:**
  - Default number of lives
  - Max Strength amplifier
  - Choose whether to ban or set players to spectator on zero lives
- 🔒 **Persistent data:** Player stats are saved automatically using Bukkit’s `PersistentDataContainer`.

---

## 🧠 How It Works

- Each time a player kills another, their **kill count increases**, and they receive a **Strength potion effect**.  
- The Strength level is capped based on the configuration file.  
- When a player dies, their **lives** decrease by one.
- Reaching 0 lives will:
  - Remove all buffs
  - Optionally switch to Spectator Mode or ban the player

---

## ⚙️ Configuration

The plugin generates a `config.yml` file on first launch.  
Here’s an example configuration:

```yaml
# Default number of lives for new players
default-lives: 3

# Maximum Strength level a player can reach
max-strength-amplifier: 3

# Whether players should be banned when they run out of lives
ban-on-zero-lives: false

# Whether to switch players to spectator mode instead of banning
spectator-on-zero-lives: true
