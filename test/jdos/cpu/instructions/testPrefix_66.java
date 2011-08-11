package jdos.cpu.instructions;

import jdos.cpu.CPU;
import jdos.cpu.CPU_Regs;
import jdos.cpu.Flags;
import jdos.hardware.Memory;
import jdos.util.Ptr;

public class testPrefix_66 extends InstructionsTestCase{
    // 0x201
    // ADD Ed,Gd
    public void testAddEdGd() {
        runRegsd(0x201, 1001, 2, false, 1003, 2002);
        runRegd(0x201, 1, -2, false, -1);
        assertTrue(!Flags.get_CF());
        runRegd(0x201, 0x80000000, 0x80000000, false, 0);
        assertTrue(Flags.get_CF());
        // make sure CF doesn't affect it
        runRegd(0x201, 0x80000000, 0x80000000, false, 0);
        runRegd(0x201, 0x80000000, 0x00000001, false, 0x80000001);
    }

    // 0x203
    // ADD Gd,Ed
    public void testAddGdEd() {
        runRegsd(0x203, 1001, 2, true, 1003, 2002);
        runRegd(0x203, 1, -2, true, -1);
        assertTrue(!Flags.get_CF());
        runRegd(0x203, 0x80000000, 0x80000000, true, 0);
        assertTrue(Flags.get_CF());
        // make sure CF doesn't affect it
        runRegd(0x203, 0x80000000, 0x80000000, true, 0);
        runRegd(0x203, 0x80000000, 0x00000001, true, 0x80000001);
    }

    // 0x205
    // ADD EAX,Id
     public void testAddEaxId() {
        newInstruction(0x205);
        CPU_Regs.reg_eax.word(1);
        pushId(2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.word()==3);

        newInstruction(0x205);
        CPU_Regs.reg_eax.word(1);
        pushId(-2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword==0xFFFFFFFF);
        assertTrue(!Flags.get_CF());

        newInstruction(0x205);
        CPU_Regs.reg_eax.dword(0x80000001);
        pushId(0x80000000);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword==1);
        assertTrue(Flags.get_CF());

         // make sure CF doesn't affect it
        newInstruction(0x205);
        CPU_Regs.reg_eax.dword(0x80000002);
        pushId(0x80000000);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.word()==2);
    }

    // 0x206
    //PUSH ES
    public void testPushES() {
        newInstruction(0x206);
        CPU_Regs.reg_esp.dword(0x100);
        CPU.Segs_ESval = 0xABCD;
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword()==0xFC);
        assertTrue(new Ptr(Memory.direct, (int)(CPU.Segs_SSphys+CPU_Regs.reg_esp.dword())).readd(0)==0xABCD);
    }

    // 0x207
    //POP ES
    public void testPopES() {
        newInstruction(0x207);
        CPU_Regs.reg_esp.dword(0xFC);
        new Ptr(Memory.direct, (int)(CPU.Segs_SSphys+CPU_Regs.reg_esp.dword())).writed(0, 0x189EF);
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword()==0x100);
        assertTrue(CPU.Segs_ESval==0x89EF); // will only pop 16-bit segment
    }

    // 0x209
    //OR Ed,Gd
    public void testOrEdGd() {
        runRegsd(0x209, 1, 2, false, 3, 1);
        runRegd(0x209, 0, 0, false, 0);
        runRegd(0x209, 0xFFFFFFFF, 0, false, 0xFFFFFFFF);
        runRegd(0x209, 0, 0xFFFFFFFF, false, 0xFFFFFFFF);
        runRegd(0x209, 0xFF00FF00, 0x00FF00FF, false, 0xFFFFFFFF);
    }

    // 0x20B
    //OR Gd,Ed
    public void testOrGdEd() {
        runRegsd(0x20b, 1, 2, true, 3, 1);
        runRegd(0x20b, 0, 0, true, 0);
        runRegd(0x20b, 0xFFFFFF00, 0, true, 0xFFFFFF00);
        runRegd(0x20b, 0, 0xFFFF00FF, true, 0xFFFF00FF);
        runRegd(0x20b, 0xFF00F0F0, 0x00FF0F0F, true, 0xFFFFFFFF);
    }

    // 0x20D
    //OR EAX,Id
    public void testOrEaxId() {
        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword=1;
        pushId(2);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword==3);

        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword=0;
        pushId(0);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword==0);

        newInstruction(0x20D);
        CPU_Regs.reg_eax.dword=0xF00FF00F;
        pushId(0x0FF00FF0);
        decoder.call();
        assertTrue(CPU_Regs.reg_eax.dword==0xFFFFFFFF);
    }

    // 0x20E
    //PUSH CS
    public void testPushCS() {
        newInstruction(0x20E);
        CPU_Regs.reg_esp.dword(0x100);
        CPU.Segs_CSval = 0x1234ABCD;
        decoder.call();
        assertTrue(CPU_Regs.reg_esp.dword()==0xFC);
        assertTrue(new Ptr(Memory.direct, (int)(CPU.Segs_SSphys+CPU_Regs.reg_esp.dword())).readd(0)==0x1234ABCD);
    }
}