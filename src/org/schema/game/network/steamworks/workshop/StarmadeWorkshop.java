package org.schema.game.network.steamworks.workshop;

import com.codedisaster.steamworks.*;
import org.schema.game.server.controller.BluePrintController;
import org.schema.game.server.controller.ImportFailedException;
import org.schema.game.server.controller.MayImportCallback;
import org.schema.game.server.data.blueprintnw.BlueprintEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class StarmadeWorkshop {

    private SteamUGCCallback ugcCallback = new SteamUGCCallback() {
        @Override
        public void onUGCQueryCompleted(SteamUGCQuery query, int numResultsReturned, int totalMatchingResults, boolean isCachedData, SteamResult result) {

        }

        @Override
        public void onSubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
            if(result.equals(SteamResult.OK)) {
                workshop.downloadItem(publishedFileID, false);
            }
        }

        @Override
        public void onUnsubscribeItem(SteamPublishedFileID publishedFileID, SteamResult result) {
            if(result.equals(SteamResult.OK)) {

            }
        }

        @Override
        public void onRequestUGCDetails(SteamUGCDetails details, SteamResult result) {

        }

        @Override
        public void onCreateItem(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {
            //Todo:Create a label next to the upload button that notifies the user of the workshop user agreement, which will redirect them if clicked.
            if(result.equals(SteamResult.OK)) {
                updateHandle = workshop.startItemUpdate(appID, publishedFileID);
            }
        }

        @Override
        public void onSubmitItemUpdate(SteamPublishedFileID publishedFileID, boolean needsToAcceptWLA, SteamResult result) {

        }

        @Override
        public void onDownloadItemResult(int appID, final SteamPublishedFileID publishedFileID, SteamResult result) {
            if(appID == getAppID()) {
                SteamUGC.ItemInstallInfo itemInstallInfo = new SteamUGC.ItemInstallInfo();
                if(workshop.getItemInstallInfo(publishedFileID, itemInstallInfo)) {
                    File fileLocation = new File(itemInstallInfo.getFolder());
                    //final File workshopFile = Objects.requireNonNull(fileLocation.listFiles())[0].getAbsoluteFile();
                    for(File workshopFile : Objects.requireNonNull(fileLocation.listFiles())) {
                        final String fileName = workshopFile.getName();
                        if(workshopFile.toPath().getFileName().endsWith(".sment")) { //Workshop file is a Blueprint
                            boolean alreadyExists = false;
                            for(BlueprintEntry entry : BluePrintController.defaultBB.readBluePrints()) {
                                if(fileName.equals(entry.getName())) {
                                    alreadyExists = true;
                                    workshopFile.delete(); //Clean up old file
                                    break;
                                }
                            }
                            if(!alreadyExists) {
                                try {
                                    BluePrintController.defaultBB.importFile(workshopFile, new MayImportCallback() {
                                        @Override
                                        public void callbackOnImportDenied(BlueprintEntry blueprintEntry) {
                                            System.err.println("[WORKSHOP] ERROR: Attempted to import Workshop Blueprint file " + fileName + " to blueprint database, but was denied access");
                                        }

                                        @Override
                                        public boolean mayImport(BlueprintEntry blueprintEntry) {
                                            return true;
                                        }

                                        @Override
                                        public void onImport(BlueprintEntry blueprintEntry) {
                                            System.out.println("[WORKSHOP][INFO] Imported Workshop Blueprint file " + fileName + " successfully");
                                        }
                                    });
                                } catch (ImportFailedException | IOException e) {
                                    e.printStackTrace();
                                    System.err.println("[WORKSHOP] ERROR: Failed to import Workshop Blueprint file " + fileName + " to blueprint database");
                                }
                            } else {
                                System.err.println("[WORKSHOP] ERROR: Attempted to import Workshop Blueprint file " + fileName + " to blueprint database but the blueprint name already exists in database");
                            }
                        } else if(workshopFile.toPath().getFileName().endsWith(".smtpl")) { //Workshop file is a Template
                            File templatesFolder = new File("/templates"); //Todo: Check if this is the correct path to /templates
                            if(!templatesFolder.exists()) {
                                try {
                                    templatesFolder.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.err.println("[WORKSHOP] ERROR: Failed to create /templates folder");
                                }
                            }

                            if(new File(templatesFolder.getAbsolutePath() + "/" + workshopFile.getName()).exists()) {
                                System.err.println("[WORKSHOP] ERROR: Attempted to move Workshop Template file " + fileName + "to /templates directory but the template file name already existed");
                                workshopFile.delete(); //Clean up old file
                            } else {
                                try {
                                    Files.move(workshopFile.toPath(), templatesFolder.toPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.err.println("[WORKSHOP] ERROR: Failed to move Workshop Template file " + fileName + " to /templates directory");
                                }
                            }
                        } else if(workshopFile.toPath().getFileName().getFileName().endsWith(".jar")) { //Workshop file is a Mod
                            File modsFolder = new File("/mods"); //Todo: Check if this is the correct path to /mods
                            if(!modsFolder.exists()) {
                                try {
                                    modsFolder.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.err.println("[WORKSHOP] ERROR: Failed to create /mods folder");
                                }
                            }

                            if(new File(modsFolder.getAbsolutePath() + "/" + workshopFile.getName()).exists()) {
                                System.err.println("[WORKSHOP] ERROR: Attempted to move Workshop Mod file " + fileName + "to /mods directory but the mod file name already existed");
                                workshopFile.delete(); //Clean up old file
                            } else {
                                try {
                                    Files.move(workshopFile.toPath(), modsFolder.toPath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    System.err.println("[WORKSHOP] ERROR: Failed to move Workshop Mod file " + fileName + " to /mods directory");
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onUserFavoriteItemsListChanged(SteamPublishedFileID publishedFileID, boolean wasAddRequest, SteamResult result) {

        }

        @Override
        public void onSetUserItemVote(SteamPublishedFileID publishedFileID, boolean voteUp, SteamResult result) {

        }

        @Override
        public void onGetUserItemVote(SteamPublishedFileID publishedFileID, boolean votedUp, boolean votedDown, boolean voteSkipped, SteamResult result) {

        }

        @Override
        public void onStartPlaytimeTracking(SteamResult result) {

        }

        @Override
        public void onStopPlaytimeTracking(SteamResult result) {

        }

        @Override
        public void onStopPlaytimeTrackingForAllItems(SteamResult result) {

        }

        @Override
        public void onDeleteItem(SteamPublishedFileID publishedFileID, SteamResult result) {

        }
    };
    private int appID;
    private SteamUGC workshop;
    private SteamUGCUpdateHandle updateHandle;

    public StarmadeWorkshop() {
        workshop = new SteamUGC(ugcCallback);
        appID = 1; //Todo: get Starmade's app ID
    }

    public void createWorkshopBlueprintEntry(BlueprintEntry blueprint) throws IOException {
        workshop.createItem(appID, SteamRemoteStorage.WorkshopFileType.Community);
        workshop.setItemTitle(updateHandle, blueprint.getName());
        ArrayList<String> itemTags = new ArrayList<>();
        itemTags.add("Blueprint");
        switch(blueprint.getClassification()) {
            case CARGO:
                itemTags.add("Ship");
                itemTags.add("Cargo");
                break;
            case SCOUT:
                itemTags.add("Ship");
                itemTags.add("Scout");
                break;
            case ATTACK:
                itemTags.add("Ship");
                itemTags.add("Attacker");
                break;
            case MINING:
                itemTags.add("Ship");
                itemTags.add("Miner");
                break;
            case CARRIER:
                itemTags.add("Ship");
                itemTags.add("Carrier");
                break;
            case DEFENSE:
                itemTags.add("Ship");
                itemTags.add("Defender");
                break;
            case SUPPORT:
                itemTags.add("Ship");
                itemTags.add("Support");
                break;
            case SCAVENGER:
                itemTags.add("Ship");
                itemTags.add("Scavenger");
                break;
            /*//Todo: add a SHELL type
            case SHELL:
                itemTags.add("Ship");
                itemTags.add("Shell");
                break;
             */
             /*//Todo: add a TURRET type
            case TURRET:
                itemTags.add("Ship");
                itemTags.add("Turret");
                break;
             */
            case NONE:
                itemTags.add("Ship");
                break;
            case NONE_STATION:
            case WAYPOINT_STATION:
                itemTags.add("Station");
                break;
            case TRADE_STATION:
            case SHOPPING_STATION:
                itemTags.add("Station");
                itemTags.add("Trading");
                break;
            case MINING_STATION:
                itemTags.add("Station");
                itemTags.add("Mining");
                break;
            case DEFENSE_STATION:
                itemTags.add("Station");
                itemTags.add("Defense");
                break;
            case FACTORY_STATION:
                itemTags.add("Station");
                itemTags.add("Factory");
                break;
            case OUTPOST_STATION:
                itemTags.add("Station");
                itemTags.add("Outpost");
                break;
            case SHIPYARD_STATION:
                itemTags.add("Station");
                itemTags.add("Shipyard");
                break;
        }
        itemTags.trimToSize();
        workshop.setItemTags(updateHandle , (String[]) itemTags.toArray());
        //Todo: Use the mesh or model export function to create preview image
        //workshop.setItemPreview(updateHandle, blueprintPreviewFile);
        String classString = itemTags.get(1);
        if(itemTags.size() > 2) {
            classString = (itemTags.get(2) + " " + itemTags.get(1));
        }

        String massString = (int) blueprint.getMass() + "";

        String descString = (
                classString + "\n"
                + "Info:\n"
                + "- Mass: " + massString + "\n"
                + "- Price: " + (int) blueprint.getPrice() + "\n"
                + "- Resources: \n" + blueprint.getElementCountMapWithChilds().getResourceString()
                );

        workshop.setItemDescription(updateHandle, descString);
        workshop.setItemContent(updateHandle, blueprint.export().getAbsolutePath());
        workshop.submitItemUpdate(updateHandle, "");
    }

    public SteamUGC getWorkshop() {
        return workshop;
    }

    public int getAppID() {
        return appID;
    }
}
