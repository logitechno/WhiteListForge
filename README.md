<div align="center">

### **🌿 WhitelistDB – A Whitelist Mod with SQLite**

</div>

**WhitelistDB** is a Minecraft (NeoForge) mod that uses an **SQLite** database to store whitelisted players.

---  

### **📜 Commands:**
| Command                 | Description                              |  
|-------------------------|------------------------------------------|  
| `/wl add/remove <name>` | Add/Remove a player from the whitelist   |  
| `/wl list`              | Displays the list of whitelisted players |  
| `/wl on/off`            | Enables/Disables the whitelist           |  

---  

### **⚙️ Configuration**

The configuration file is located at: `config/whitelistforge-common.toml`

| Parameter           | Description                                                   |  
|---------------------|---------------------------------------------------------------|  
| `whitelist_enabled` | Enables/Disables the whitelist                                |  
| `kick_reason`       | The message displayed to a player who is not on the whitelist |