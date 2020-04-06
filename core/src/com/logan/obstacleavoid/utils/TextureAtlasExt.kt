package com.logan.obstacleavoid.utils

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

// if you want to use ()
//inline operator fun TextureAtlas.invoke(regionName: String):TextureRegion? = findRegion(regionName)

// if you want to use []
inline operator fun TextureAtlas.get(regionName: String):TextureRegion? = findRegion(regionName)
