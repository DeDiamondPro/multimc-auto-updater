package io.github.dediamondpro.autoupdater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.dediamondpro.autoupdater.utils.FileUtils;
import io.github.dediamondpro.autoupdater.utils.WebUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static Pattern githubPattern = Pattern.compile("(https://)?(github\\.com/)?(?<user>[\\w-]{0,39})(/)(?<repo>[\\w-]{0,40})(.*)");

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2)
            throw new IllegalArgumentException("No Minecraft location or config location specified!");
        File configFile = new File(args[0]);
        String configRaw = FileUtils.readFile(configFile);
        JsonParser parser = new JsonParser();
        JsonObject config = parser.parse(configRaw).getAsJsonObject();
        File modsFolder = new File(args[1], "mods");
        if (!modsFolder.exists() || !modsFolder.isDirectory())
            throw new IllegalArgumentException("Mods folder not found! " + modsFolder.getAbsolutePath());
        if (!config.has("mods"))
            throw new IllegalArgumentException("No mods part found in config!");
        JsonArray mods = config.getAsJsonArray("mods");
        for (JsonElement element : mods) {
            JsonObject object = element.getAsJsonObject();

            String mustInclude = null;
            if (object.has("includes"))
                mustInclude = object.get("includes").getAsString();
            boolean usePreRelease;
            if (object.has("usePre"))
                usePreRelease = object.get("usePre").getAsBoolean();
            else
                usePreRelease = false;
            if (object.has("github")) {
                Matcher githubMatcher = githubPattern.matcher(object.get("github").getAsString());
                if (githubMatcher.matches()) {
                    String latestUrl = null;
                    String latestName = null;
                    File oldMod = null;
                    System.out.println("Fetching " + object.get("github").getAsString());
                    JsonElement releases = WebUtils.getRequest("https://api.github.com/repos/" + githubMatcher.group("user")
                            + "/" + githubMatcher.group("repo") + "/releases");
                    if (releases != null) {
                        for (JsonElement release : releases.getAsJsonArray()) {
                            for (JsonElement asset : release.getAsJsonObject().get("assets").getAsJsonArray()) {
                                String updateUrl;
                                updateUrl = asset.getAsJsonObject().get("browser_download_url").getAsString();
                                String name = updateUrl.substring(updateUrl.lastIndexOf("/") + 1);
                                File mod = new File(modsFolder, name.replace("/", "-"));
                                if (mod.exists()) {
                                    oldMod = mod;
                                    break;
                                } else if (oldMod == null && latestUrl == null && (!release.getAsJsonObject().get("prerelease").getAsBoolean() || usePreRelease)
                                        && (mustInclude == null || name.contains(mustInclude))) {
                                    latestUrl = updateUrl;
                                    latestName = name.replace("/", "-");
                                }
                            }
                        }
                        if (latestUrl != null) {
                            if (oldMod == null || oldMod.delete()) {
                                WebUtils.downloadFile(latestUrl, new File(modsFolder, latestName));
                                System.out.println("Downloaded update!");
                            } else
                                System.out.println("Could not delete " + oldMod.getName() + " this mod will not be updated!");
                        } else
                            System.out.println("Up to date!");
                    }
                } else
                    throw new IllegalArgumentException("Invalid GitHub link!!");
            } else if (object.has("modrinth")) {
                JsonElement releases = WebUtils.getRequest("https://api.modrinth.com/api/v1/mod/" + object.get("modrinth").getAsString() + "/version");
                if (releases != null) {
                    System.out.println("Fetching releases for modrinth mod id " + object.get("modrinth").getAsString());
                    String latestUrl = null;
                    String latestName = null;
                    File oldMod = null;
                    String versionType = "release";
                    if (object.has("version_type"))
                        versionType = object.get("version_type").getAsString();
                    for (JsonElement release : releases.getAsJsonArray()) {
                        String version = release.getAsJsonObject().get("version_type").getAsString();
                        if (versionType.equals(version) || versionType.equals("beta") && version.equals("release")
                                || versionType.equals("alpha") && version.equals("release") || versionType.equals("alpha") && version.equals("beta")) {
                            for (JsonElement element1 : release.getAsJsonObject().getAsJsonArray("loaders")) {
                                if (!object.has("loader") || element1.getAsString().equals(object.get("loader").getAsString())) {
                                    for (JsonElement element2 : release.getAsJsonObject().getAsJsonArray("game_versions")) {
                                        if (!object.has("mc_version") || element2.getAsString().equals(object.get("mc_version").getAsString())) {
                                            for (JsonElement asset : release.getAsJsonObject().get("files").getAsJsonArray()) {
                                                String updateUrl;
                                                updateUrl = asset.getAsJsonObject().get("url").getAsString();
                                                String name = asset.getAsJsonObject().get("filename").getAsString();
                                                File mod = new File(modsFolder, name.replace("/", "-"));
                                                if (mod.exists()) {
                                                    oldMod = mod;
                                                    break;
                                                } else if (oldMod == null && latestUrl == null && (mustInclude == null || name.contains(mustInclude))) {
                                                    latestUrl = updateUrl;
                                                    latestName = name.replace("/", "-");
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (latestUrl != null) {
                        if (oldMod == null || oldMod.delete()) {
                            WebUtils.downloadFile(latestUrl, new File(modsFolder, latestName));
                            System.out.println("Downloaded update!");
                        } else
                            System.out.println("Could not delete " + oldMod.getName() + " this mod will not be updated!");
                    } else
                        System.out.println("Up to date!");
                }
            }
        }
    }
}
