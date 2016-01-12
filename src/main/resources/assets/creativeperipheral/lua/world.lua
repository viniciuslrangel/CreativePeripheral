world = {}
local p = peripheral.find("Creative")
function world.getAll()
    return p.getWorlds()
end
function world.get(dimensionId)
    if type(dimensionId) ~= "number" then
        error("world.get(number id)")
    end
    local id = nil
    for k,v in pairs(world.getAll()) do
        if v == dimensionId then
            id = v
        end
    end
    if id == nil then
        error("Invalid world id")
    end

    local obj = {}

    function obj:getTime()
        return p.getWorldTime(id)
    end
    function obj:getSeed()
        return p.getWorldSeed(id)
    end
    function obj:getBlock(coords)
        return block.get(id, coords.x, coords.y, coords.z)
    end

end