package fr.edouwarden.space.player;

import fr.edouwarden.space.power.Power;
import fr.edouwarden.space.station.Property;
import fr.edouwarden.space.ship.ShipEntity;
import fr.edouwarden.space.variables.General;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerEntity implements Serializable {
    public static HashMap<UUID, PlayerEntity> link = new HashMap<>();
    public Player bukkitPL;
    private JobsDatas jobsDatas;// TODO: make private
    private Power power;
    private Long bounty;
    private Byte voiceRange;
    //private List sneak;
    private Short linkedMap;
    private Integer ko;
    private Player grab;
    private PlayerEntity grabbed; //by who
    private Location grabloc;
    public boolean  cuffed;

    private List propertys;
    transient ShipEntity ownedShip;
    private int ownedShipType;
    private String ownedShipSkin;
    private boolean inSpace;
    private Long cooldown;
    private List abilitiesSwitch;// TODO: temporary field
    private List abilities;
    private List key;
    transient ShipEntity onShip;


    public PlayerEntity(Player bukkitPL ) {
        this.bukkitPL = bukkitPL;
        this.jobsDatas = new JobsDatas();
        power = null;
        bounty = 0L;
        voiceRange = 1;
        ownedShipType = 1;
        linkedMap = null;
        ko = null;
        grab = null;
        grabbed = null;
        grabloc = null;
        cuffed = false;

        propertys = new ArrayList();
        ownedShip = null;
        inSpace = false;
        cooldown=null;
        abilitiesSwitch=null;
        abilities=null;
        key=null;

        addPlayerEntity(this);


    }
    public static PlayerEntity getPlayerEntity(Player player){
        return link.get(player.getUniqueId());
    }
    public static void addPlayerEntity(PlayerEntity player){
        link.put(player.getBukkitPL().getUniqueId(),player);
    }






























    public List         getSwitchNonAbilities(){                      return this.abilitiesSwitch;}// TODO: temporary field
    public void         setSwitchNonAbilities(List nonabilities){       this.abilitiesSwitch = nonabilities;}// TODO: temporary field
    public boolean      haveSwitchNonAbilities(){       return this.abilitiesSwitch!=null;}// TODO: temporary field
    public Player       getBukkitPL(){                      return this.bukkitPL;}
    public void         setPower(Power power){       this.power = power;}
    public boolean      hasPower(){                   return this.power!=null;}
    public Power           getPower(){                    return this.power;}
    public void         clearPower(){                  this.power = null;}
    public boolean      haveCooldowns(){                    return this.cooldown!=null;}
    public void         setcooldown(Long cooldown){         this.cooldown = cooldown;}
    public Long         getcooldown(){                      return this.cooldown;}
    public void         setbounty(Long bounty){             this.bounty = bounty;}
    public Long         getbounty(){                        return this.bounty;}
    public boolean      hasBounty(){                        return this.bounty != null && this.bounty != 0L;}
    public void         setVoiceRange(Byte voiceRange) {    this.voiceRange = voiceRange; }
    public Byte         getVoiceRange() {                   return voiceRange; }
    public boolean      isMute() {                          return this.voiceRange==0; }
    public void         setLinkedMap(Short linkedMap) {     this.linkedMap = linkedMap; }
    public Short        getLinkedMap() {                    return linkedMap; }
    public boolean      haveLinkedMap() {                   return linkedMap!=null; }
    public void         setKo(Integer ko) {                 this.ko = ko; }
    public boolean      isKo(){                             return this.ko!=null ;}
    public Integer      getKo() {                           return ko; }
    public void         setGrab(Player grab) {              this.grab = grab; }
    public Player       getGrab() {                         return grab; }
    public boolean      isGrabber() {                       return this.grab !=null; }
    public void         setGrabber(PlayerEntity grabbed) {this.grabbed = grabbed; }
    public PlayerEntity getGrabber() {                    return grabbed; }
    public boolean      isGrabbed() {                       return grabbed!=null; }
    public void         setGrabloc(Location grabloc) {      this.grabloc = grabloc; }
    public Location     getGrabloc() {                      return grabloc; }
    public void         setPropertys(List propertys) {      this.propertys = propertys; }
    public List         getPropertys() {                    return propertys; }
    public void         addPropertys(Property property) {   this.propertys.add(property); }
    public void         setOwnedShip(ShipEntity ownedShip) {this.ownedShip = ownedShip; }
    public ShipEntity   getOwnedShip() {                    return ownedShip; }
    public boolean      haveShip(){                         return this.ownedShip !=null;}
    public void         setInSpace(boolean inSpace) {       this.inSpace = inSpace; }
    public boolean      isInSpace() {                        return inSpace; }
    public JobsDatas    getJobDatas() {                     return jobsDatas; }
    public void         setJobDatas(JobsDatas jobsDatas) {  this.jobsDatas=jobsDatas; }
    public Integer      getOwnedShipType() {                return ownedShipType; }
    public void         setShipTypes(Integer type) {        this.ownedShipType =type; }
    public void         leave(){                            link.remove(this.getBukkitPL().getUniqueId());}

    public double getMoney(){return General.econ.getBalance(this.bukkitPL);}
    public void setMoney(double money){General.econ.withdrawPlayer(this.bukkitPL,getMoney());
    General.econ.depositPlayer(this.bukkitPL,money);}
    public boolean pay(double price){
        EconomyResponse response = General.econ.withdrawPlayer(this.bukkitPL,price);
        if(response.transactionSuccess())return true;
        return false;
    }


    public void navigatorOf(ShipEntity ship) { this.onShip = ship;
    }

    public ShipEntity navigatorOn() {
        return this.onShip;
    }

    public static void execRPK(Player player) {
        new PlayerEntity(player);

    }
}
