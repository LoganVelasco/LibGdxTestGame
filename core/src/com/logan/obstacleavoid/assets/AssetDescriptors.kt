package com.logan.obstacleavoid.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.logan.obstacleavoid.utils.assetDescriptor

object AssetDescriptors {
    val FONT = assetDescriptor<BitmapFont> (AssetPaths.MY_FONT)
    val GAMEPLAY = assetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY)
}