From 919f607ee29d482599be9abf1525c6d79413a44f Mon Sep 17 00:00:00 2001
From: mrapple <tony@oc.tc>
Date: Tue, 18 Jun 2013 22:02:04 -0500
Subject: [PATCH] Chunk Authority

---
 src/main/java/org/bukkit/ChunkAuthority.java |    5 +++++
 src/main/java/org/bukkit/World.java          |    4 ++++
 2 files changed, 9 insertions(+)
 create mode 100644 src/main/java/org/bukkit/ChunkAuthority.java

diff --git a/src/main/java/org/bukkit/ChunkAuthority.java b/src/main/java/org/bukkit/ChunkAuthority.java
new file mode 100644
index 0000000..4698116
--- /dev/null
+++ b/src/main/java/org/bukkit/ChunkAuthority.java
@@ -0,0 +1,5 @@
+package org.bukkit;
+
+public interface ChunkAuthority {
+	public boolean hasAuthority(int x, int z);
+}
diff --git a/src/main/java/org/bukkit/World.java b/src/main/java/org/bukkit/World.java
index aabd5b7..6543035 100644
--- a/src/main/java/org/bukkit/World.java
+++ b/src/main/java/org/bukkit/World.java
@@ -1095,6 +1095,10 @@ public interface World extends PluginMessageRecipient, Metadatable {
      */
     public boolean isGameRule(String rule);
 
+    public void setChunkAuthority(ChunkAuthority chunkAuthority);
+
+    public ChunkAuthority getChunkAuthority();
+
     /**
      * Represents various map environment types that a world may be
      */
-- 
1.7.9.6 (Apple Git-31.1)

