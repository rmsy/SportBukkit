From ce8c5f001e8cb73ab2161d2cea2d489b5c46c744 Mon Sep 17 00:00:00 2001
From: mrapple <tony@oc.tc>
Date: Tue, 18 Jun 2013 22:10:02 -0500
Subject: [PATCH] Chunk Authority

---
 .../net/minecraft/server/ChunkProviderServer.java  |    8 ++++++++
 src/main/java/net/minecraft/server/Entity.java     |    2 ++
 .../bukkit/craftbukkit/CraftChunkAuthority.java    |   10 ++++++++++
 .../java/org/bukkit/craftbukkit/CraftWorld.java    |   10 ++++++++++
 4 files changed, 30 insertions(+)
 create mode 100644 src/main/java/org/bukkit/craftbukkit/CraftChunkAuthority.java

diff --git a/src/main/java/net/minecraft/server/ChunkProviderServer.java b/src/main/java/net/minecraft/server/ChunkProviderServer.java
index b73f69c..e2dd267 100644
--- a/src/main/java/net/minecraft/server/ChunkProviderServer.java
+++ b/src/main/java/net/minecraft/server/ChunkProviderServer.java
@@ -85,6 +85,8 @@ public class ChunkProviderServer implements IChunkProvider {
     }
 
     public Chunk getChunkAt(int i, int j, Runnable runnable) {
+        if(!this.world.getWorld().getChunkAuthority().hasAuthority(i, j)) return null;
+
         this.unloadQueue.remove(i, j);
         Chunk chunk = (Chunk) this.chunks.get(LongHash.toLong(i, j));
         boolean newChunk = false;
@@ -153,6 +155,8 @@ public class ChunkProviderServer implements IChunkProvider {
 
     public Chunk getOrCreateChunk(int i, int j) {
         // CraftBukkit start
+        if(!this.world.getWorld().getChunkAuthority().hasAuthority(i, j)) return null;
+
         Chunk chunk = (Chunk) this.chunks.get(LongHash.toLong(i, j));
 
         chunk = chunk == null ? (!this.world.isLoading && !this.forceChunkLoad ? this.emptyChunk : this.getChunkAt(i, j)) : chunk;
@@ -169,6 +173,8 @@ public class ChunkProviderServer implements IChunkProvider {
     }
 
     public Chunk loadChunk(int i, int j) { // CraftBukkit - private -> public
+        if(!this.world.getWorld().getChunkAuthority().hasAuthority(i, j)) return null; // CraftBukkit
+
         if (this.e == null) {
             return null;
         } else {
@@ -217,6 +223,8 @@ public class ChunkProviderServer implements IChunkProvider {
     }
 
     public void getChunkAt(IChunkProvider ichunkprovider, int i, int j) {
+        if(!this.world.getWorld().getChunkAuthority().hasAuthority(i, j)) return; // CraftBukkit
+
         Chunk chunk = this.getOrCreateChunk(i, j);
 
         if (!chunk.done) {
diff --git a/src/main/java/net/minecraft/server/Entity.java b/src/main/java/net/minecraft/server/Entity.java
index 9ccce59..0cf1ea3 100644
--- a/src/main/java/net/minecraft/server/Entity.java
+++ b/src/main/java/net/minecraft/server/Entity.java
@@ -1297,6 +1297,8 @@ public abstract class Entity {
     }
 
     public void T() {
+        // CraftBukkit start
+        if(!this.world.getWorld().getChunkAuthority().hasAuthority((int) this.locX >> 4, (int) this.locZ >> 4)) return; // CraftBukkit
         if (this.vehicle.dead) {
             this.vehicle = null;
         } else {
diff --git a/src/main/java/org/bukkit/craftbukkit/CraftChunkAuthority.java b/src/main/java/org/bukkit/craftbukkit/CraftChunkAuthority.java
new file mode 100644
index 0000000..4465c75
--- /dev/null
+++ b/src/main/java/org/bukkit/craftbukkit/CraftChunkAuthority.java
@@ -0,0 +1,10 @@
+package org.bukkit.craftbukkit;
+
+import org.bukkit.Chunk;
+import org.bukkit.ChunkAuthority;
+
+public class CraftChunkAuthority implements ChunkAuthority {
+	public boolean hasAuthority(int x, int z) {
+		return true;
+	}
+}
\ No newline at end of file
diff --git a/src/main/java/org/bukkit/craftbukkit/CraftWorld.java b/src/main/java/org/bukkit/craftbukkit/CraftWorld.java
index 0cd1867..16685c8 100644
--- a/src/main/java/org/bukkit/craftbukkit/CraftWorld.java
+++ b/src/main/java/org/bukkit/craftbukkit/CraftWorld.java
@@ -16,6 +16,7 @@ import org.apache.commons.lang.Validate;
 import org.bukkit.BlockChangeDelegate;
 import org.bukkit.Bukkit;
 import org.bukkit.Chunk;
+import org.bukkit.ChunkAuthority;
 import org.bukkit.ChunkSnapshot;
 import org.bukkit.Difficulty;
 import org.bukkit.Effect;
@@ -60,6 +61,7 @@ public class CraftWorld implements World {
     private final ChunkGenerator generator;
     private final List<BlockPopulator> populators = new ArrayList<BlockPopulator>();
     private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
+    private ChunkAuthority chunkAuthority = new CraftChunkAuthority();
     private int monsterSpawn = -1;
     private int animalSpawn = -1;
     private int waterAnimalSpawn = -1;
@@ -1273,6 +1275,14 @@ public class CraftWorld implements World {
         return getHandle().getGameRules().e(rule);
     }
 
+    public ChunkAuthority getChunkAuthority() {
+        return this.chunkAuthority;
+    }
+
+    public void setChunkAuthority(ChunkAuthority chunkAuthority) {
+        this.chunkAuthority = chunkAuthority;
+    }
+
     public void processChunkGC() {
         chunkGCTickCount++;
 
-- 
1.7.9.6 (Apple Git-31.1)

