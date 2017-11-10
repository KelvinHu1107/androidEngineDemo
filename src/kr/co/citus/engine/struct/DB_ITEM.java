package kr.co.citus.engine.struct;

public class DB_ITEM {
	public int	x;							///< POI��ġ ��ǥ
	public int	y;							///< POI��ġ ��ǥ
	public int	kind;						///< �����ڵ�
	public short	length;						///< ���ڿ� ����
	public int		addr1;				///< �ּ��ڵ�
	public int		addr2;				///< �ּ��ڵ�
	public int		addr3;				///< �ּ��ڵ�
	public int		dist;						///< ������ġ������ �Ÿ�(�ݰ�˻��� ��쿡��)
	public short	tel_range;					///< ��ȭ��ȣ Range��
	public int		bodyIdx;					///< DB Body�� Index
	public int		detail;						// �������� �������� ���� ������(114��)
	public int		match;						// ��Ī ��Ȯ�� (114��)
	public int		telIdx;						///< telIdx;
	public int		accuracy;					///< POI ��Ȯ�� 50m �̳�, 100m �̸� 100m �̻�
	public int		angle;						//< ���� ���� ��ġ�� POI���� ����
	public int		group_id;
	public int		group_cnt;
	public int		ori_len;
	public int		croad_id;
	public String	ubcode;
	public String	tel_ori;
	public String	addr_str;
	public String	name;					///< �̸� (����ũ����)
	public String 	strAddr;
	public String	strTel;
	
	public int		dbType;

	public DB_ITEM()	{ init(); }
	
	public DB_ITEM(String _name)
	{
		name = _name;
	}
	public DB_ITEM(DB_ITEM _item)
	{
		x = _item.x;
		y = _item.y;
		kind = _item.kind;
		length = _item.length;
		addr1 = _item.addr1;
		addr2 = _item.addr2;
		addr3 = _item.addr3;
		dist = _item.dist;
		tel_range = _item.tel_range;
		bodyIdx = _item.bodyIdx;
		detail = _item.detail;
		match = _item.match;
		telIdx = _item.telIdx;
		accuracy = _item.accuracy;
		angle = _item.angle;
		group_id = _item.group_id;
		group_cnt = _item.group_cnt;
		ori_len = _item.ori_len;
		dbType = _item.dbType;
		name = new String(_item.name);
		if(_item.strAddr != null)
			strAddr = new String(_item.strAddr);
		else
			strAddr = new String("");
		if(_item.strTel != null)
			strTel = new String(_item.strTel);
		else
			strTel = new String("");
		
		croad_id = _item.croad_id;
		if(_item.ubcode != null)
			ubcode = new String(_item.ubcode);
		else
			ubcode = new String("");
		if(_item.tel_ori != null)
			tel_ori = new String(_item.tel_ori);
		else
			tel_ori = new String("");
		if(_item.addr_str != null)
			addr_str = new String(_item.addr_str);
		else
			addr_str = new String("");
	}
	public DB_ITEM(DB_ITEM _item, String addr, String tel)
	{
		dbType = _item.dbType;
		x = _item.x;
		y = _item.y;
		kind = _item.kind;
		length = _item.length;
		addr1 = _item.addr1;
		addr2 = _item.addr2;
		addr3 = _item.addr3;
		dist = _item.dist;
		tel_range = _item.tel_range;
		bodyIdx = _item.bodyIdx;
		detail = _item.detail;
		match = _item.match;
		telIdx = _item.telIdx;
		accuracy = _item.accuracy;
		angle = _item.angle;
		group_id = _item.group_id;
		group_cnt = _item.group_cnt;
		ori_len = _item.ori_len;
		dbType = _item.dbType;
		name = new String(_item.name);
		strAddr = new String(addr);
		strTel = new String(tel);
		croad_id = _item.croad_id;
		if(_item.ubcode != null)
			ubcode = new String(_item.ubcode);
		else
			ubcode = new String("");
		if(_item.tel_ori != null)
			tel_ori = new String(_item.tel_ori);
		else
			tel_ori = new String("");
		if(_item.addr_str != null)
			addr_str = new String(_item.addr_str);
		else
			addr_str = new String("");
	}
	void init()
	{
		dbType = -1;
		x = 0;
		y = 0;
		kind = 0;
		length = 0;
		addr1 = 0;
		addr2 = 0;
		addr3 = 0;
		dist = 0;
		tel_range = 0;
		bodyIdx = 0;
		detail = 0;
		match = 0;
		telIdx = 0;
		accuracy = 0;
		angle = 0;
		group_id = 0;
		group_cnt = 0;
		ori_len = 0;
		croad_id = 0;
		name = null;
		strAddr = null;
		strTel = null;
		tel_ori = null;
		addr_str = null;
		ubcode = null;
	}
}
